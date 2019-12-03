package com.qiang.practice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiang.practice.base.TipsConstants;
import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.mapper.PlateInfoMapper;
import com.qiang.practice.mapper.PlateMapper;
import com.qiang.practice.model.Plate;
import com.qiang.practice.utils.NumUtil;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.RUtil;
import com.qiang.practice.utils.response.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/1
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class PlateServiceImpl implements PlateService {
    @Autowired
    private PlateMapper plateMapper;
    @Autowired
    private PlateInfoMapper plateInfoMapper;
    @Autowired
    private ServerConfig serverConfig;

    @Override
    public R getByPage(Map<String, Object> paramMap) {
        Long start = System.currentTimeMillis();
        //打印请求者ip
//        System.out.println("mac ===>  " + IPUtils.getMacAddrByIp(IPUtils.getClientIpAddr(HttpContextUtils.getHttpServletRequest())));
//        System.out.println("ip ===>  " + IPUtils.getClientIpAddr(HttpContextUtils.getHttpServletRequest()));
        //使用分页插件
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()), Integer.valueOf(paramMap.get("pageSize").toString()));
        //根据动态条件查询对应的企业印象
        List<Map<String, Object>> plateList = plateMapper.getListByPage(paramMap);
        //为每个图片的url加上ip和端口
        for (Map<String, Object> plate : plateList) {
            plate.put("plateIcon", serverConfig.getImageUrl(plate.get("plateIcon").toString()));
        }
        //将结果进行分页封装
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(plateList);
        System.out.println(System.currentTimeMillis() - start);
        return R.ok(pageInfo);
    }

    @Override
    public R addOrUpdate(HttpServletRequest request, Plate plate) {
        if (!checkPlateNameExist(plate.getPlateName(), plate.getId()).isRes()) {
            request.setAttribute("notLog", 1);
            return R.error(TipsConstants.PLATE_NAME_HAS_EXISTED);
        }
        int rows = 0;
        //如果id为null，表示为新增，否则为修改
        if (null == plate.getId()) {
            request.setAttribute("logType", "新建");
            plate.setCreateDate(new Date());
            plate.setIsValid((byte) 1);
            //在已经有的最大排序号上加1
            plate.setOrderby(NumUtil.getOrderbyByMaxOrderby(plateMapper.getMaxOrderby()));
            //新增
            rows = plateMapper.insert(plate);
        } else {
            request.setAttribute("logType", "编辑");
            //修改操作，设置修改时间
            plate.setUpdateDate(new Date());
            //修改
            rows = plateMapper.updateByPrimaryKeySelective(plate);
        }
        //返回结果
        return RUtil.isOptionOk(rows);
    }

    @Override
    public R getById(Long id) {
        //根据id查单个企业印象的详细信息
        Plate plate = plateMapper.selectByPrimaryKey(id);
        //图片url拼串
        plate.setPlateImg(serverConfig.getImageUrl(plate.getPlateImg()));
        plate.setPlateIcon(serverConfig.getImageUrl(plate.getPlateIcon()));
        return R.ok(plate);
    }

    @Override
    public R removeByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量删除板块,将有效字段置为0（在if语句中）
        if (plateMapper.updateIsValidToZeroByIdList(idList) == idList.size()) {
            //删除完板块再删除相应板块下的信息
            plateInfoMapper.updateIsValidToZeroByPlateIdList(idList);
        }
        return R.okDefault();
    }

    @Override
    public R publishByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量启用板块,将发布字段置为1
        return RUtil.isBatchOk(plateMapper.updateIsPublishToOneByIdList(idList), idList.size());
    }

    @Override
    public R revokeByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量撤销板块,将发布字段置为0
        return RUtil.isBatchOk(plateMapper.updateIsPublishToZeroByIdList(idList), idList.size());
    }

    @Override
    public Validator checkPlateNameExist(String plateName, Long id) {
        //查看该板块名是否已存在
        return RUtil.isNameExist(plateMapper.getPlateNameCount(plateName, id));
    }

    @Override
    public R getSortData() {
        //获取需要排序的板块数据
        return R.ok(plateMapper.getSortData());
    }

    @Override
    public R sort(Map<String, Object> param) {
        List<Map<String, Object>> oldSort = (List<Map<String, Object>>) param.get("oldSortList");
        List<Map<String, Object>> newSort = (List<Map<String, Object>>) param.get("newSortList");
        int flag = 1, count = 0;
        for (int i = 0; i < oldSort.size(); i++) {
            if (!oldSort.get(i).get("id").toString().equals(newSort.get(i).get("id").toString())) {
                oldSort.get(i).put("id", newSort.get(i).get("id").toString());
                //根据id修改orderby字段
                int result = plateMapper.sort(oldSort.get(i));
                if (result != 1) {
                    flag = 0;
                }
            }
        }
        return RUtil.isOptionOk(flag);
    }

    @Override
    public R getAll() {
        //获取全部板块列表
        List<Map<String, Object>> plateList = plateMapper.getPlateList();
        for (Map<String, Object> plate : plateList) {
            plate.put("plateIcon", serverConfig.getImageUrl(plate.get("plateIcon").toString()));
            plate.put("plateImg", serverConfig.getImageUrl(plate.get("plateImg").toString()));
        }
        return R.ok(plateList);
    }
}
