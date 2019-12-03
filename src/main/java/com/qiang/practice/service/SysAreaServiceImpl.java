package com.qiang.practice.service;

import com.qiang.practice.mapper.SysAreaMapper;
import com.qiang.practice.utils.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: CLQ
 * @Date: 2019/8/26
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class SysAreaServiceImpl implements SysAreaService {
    @Autowired
    private SysAreaMapper sysAreaMapper;
    @Override
    public R getById(Integer id) {
        return R.ok(sysAreaMapper.selectByPrimaryKey(id));
    }

    @Override
    public R getByPId(Integer pId) {
        return R.ok(sysAreaMapper.getByPId(pId));
    }
}
