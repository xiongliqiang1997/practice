package com.qiang.practice.mapper;

import com.qiang.practice.model.ProductType;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductTypeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductType record);

    int insertSelective(ProductType record);

    ProductType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductType record);

    int updateByPrimaryKey(ProductType record);

    /**
     * 根据条件查询
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> getByCondition(Map<String, Object> paramMap);

    /**
     * 根据上级分类id获取节点
     * @param pId
     * @return
     */
    List<ProductType> getByPId(long pId);

    /**
     * 获取最大排序号
     * @return
     */
    Integer getMaxOrderby();

    /**
     * 获取其父级分类下该分类名称的数量
     * @param typeName
     * @param id
     * @return
     */
    int getTypeNameCount(@Param("typeName") String typeName,
                         @Param("id") Long id,
                         @Param("pId") Long pId);

    /**
     * 批量删除商品分类,将每个分类及其子分类的有效字段置为0
     * @param id
     */
    void updateIsValidToZeroById(Long id);

    /**
     * 根据id查单个商品分类的详细信息
     * @param id
     * @return
     */
    Map<String, Object> getById(Long id);

    /**
     * 根据id查询其下第一层子分类，若id为null，则默认查一级分类
     * @param id
     * @return
     */
    List<Map<String, Object>> getFirstSonById(Long id);

    /**
     * 根据id修改orderby字段
     * @param stringObjectMap
     * @return
     */
    int sort(Map<String, Object> stringObjectMap);
}