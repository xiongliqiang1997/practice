package com.qiang.practice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiang.practice.base.Constants;
import com.qiang.practice.base.TipsConstants;
import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.mapper.OrgSceneryMapper;
import com.qiang.practice.model.OrgScenery;
import com.qiang.practice.utils.NumUtil;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.RUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/7/10
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class OrgSceneryServiceImpl implements OrgSceneryService {
    @Autowired
    private OrgSceneryMapper orgSceneryMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ServerConfig serverConfig;

    @Override
    public R getByPage(Map<String, Object> paramMap) {
        //可以将图片的url前缀传到sql中，然后使用concat进行拼串，但是发现效率低于java拼串
        //paramMap.put("imageUrlPrefix", getLoadImageUrlPrefix());
        //使用分页插件
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()), Integer.valueOf(paramMap.get("pageSize").toString()));
        //根据条件分页查询对应的企业风光
        List<Map<String, Object>> orgSceneryList = orgSceneryMapper.getListByCondition(paramMap);
        //为每个图片的url加上ip和端口
        for (Map<String, Object> orgScenery : orgSceneryList) {
            //测试后发现使用java进行字符串拼接，比mysql的concat函数拼串效率高，且性能稳定
            //只是循环过程中会加大对jvm内存的占用，当数据量过大时，应对两者进行取舍
            orgScenery.put("imgPath", serverConfig.getImageUrl(orgScenery.get("imgPath").toString()));
        }
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(orgSceneryList);
        return R.ok(pageInfo);
    }

    @Override
    public R addOrUpdate(HttpServletRequest request, OrgScenery orgScenery) {
        int rows = 0;
        //如果id为null，表示为新增，否则为修改
        if (null == orgScenery.getId()) {
            request.setAttribute("logType", "新建");
            //设置创建时间
            orgScenery.setCreateDate(new Date());
            //设置创建人
            orgScenery.setSysUser(redisService.findByToken(request.getHeader(Constants.TOKEN_HEADER_PARAM)).getId());
            //先查一下目前的最大排序号, 然后在此最大排序号基础上+1
            orgScenery.setOrderby(NumUtil.getOrderbyByMaxOrderby(orgSceneryMapper.getMaxOrderby()));
            //新增企业风光
            rows = orgSceneryMapper.add(orgScenery);
        } else {
            request.setAttribute("logType", "编辑");
            //修改操作，设置修改时间
            orgScenery.setUpdateDate(new Date());
            //修改企业风光信息（只修改已经修改过的字段，没有改的字段不做操作）
            rows = orgSceneryMapper.updateByPrimaryKeySelective(orgScenery);
        }
        //返回结果
        return RUtil.isOptionOk(rows);
    }

    @Override
    public R removeByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量将这些企业风光的有效字段设置为0，即无效
        int rows = orgSceneryMapper.updateValidToZeroByIdList(idList);
        //返回结果
        return RUtil.isBatchOk(rows, idList.size());
    }

    @Override
    public R getById(Long id) {
        OrgScenery orgScenery = orgSceneryMapper.getById(id);
        orgScenery.setImgPath(serverConfig.getImageUrl(orgScenery.getImgPath()));
        return R.ok(orgScenery);
    }

    @Override
    public R getSortData() {
        //获取需要排序的企业风光数据
        return R.ok(orgSceneryMapper.getSortData());
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
                int result = orgSceneryMapper.sort(oldSort.get(i));
                if (result != 1) {
                    flag = 0;
                }
            }
        }
        return RUtil.isOptionOk(flag);
    }

    @Override
    public R getAll() {
        //全查：将查询条件置为null
        List<Map<String, Object>> orgSceneryList = orgSceneryMapper.getListByCondition(null);
        for (Map<String, Object> orgScenery : orgSceneryList) {
            orgScenery.put("imgPath", serverConfig.getImageUrl(orgScenery.get("imgPath").toString()));
        }
        return R.ok(orgSceneryList);
    }
}
