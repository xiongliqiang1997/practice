package com.qiang.practice.mapper;

import com.qiang.practice.model.OrgProfile;

public interface OrgProfileMapper {
    int deleteByPrimaryKey(Long id);

    int add(OrgProfile record);

    int insertSelective(OrgProfile record);

    OrgProfile selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgProfile record);

    int updateByPrimaryKeyWithBLOBs(OrgProfile record);

    int updateByPrimaryKey(OrgProfile record);

    /**
     * 获得目前的企业简介
     * @return
     */
    OrgProfile getNow();
}