package com.qiang.practice.mapper;

import com.qiang.practice.model.UserOrderLogistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserOrderLogisticsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserOrderLogistics record);

    int insertSelective(UserOrderLogistics record);

    UserOrderLogistics selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserOrderLogistics record);

    int updateByPrimaryKey(UserOrderLogistics record);

    /**
     * 批量生成物流信息
     * @param userOrderLogisticsList
     * @return
     */
    int batchInsert(List<UserOrderLogistics> userOrderLogisticsList);

    /**
     * 根据userOrderProductId数组字符串将物流状态置为已签收
     * @param userOrderProductIdListStr
     * @param logisticsHasSignedStatus
     */
    int updateStatusByUserOrderProductIdListStr(@Param("userOrderProductIdListStr") String userOrderProductIdListStr,
                                                @Param("logisticsHasSignedStatus") Byte logisticsHasSignedStatus);
}