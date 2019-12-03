package com.qiang.practice.model.vo;

import com.qiang.practice.model.SysArea;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: CLQ
 * @Date: 2019/8/26
 * @Description: TODO
 */
@Data
public class UserAddressAreaVO {
    private Long id;

    private Long sysUser;

    private String consigneeName;

    private String consigneePhone;

    private Integer province;
    private SysArea provinceObj;

    private Integer city;
    private SysArea cityObj;
    private List<SysArea> checkedCityList;

    private Integer area;
    private SysArea areaObj;
    private List<SysArea> checkedAreaList;

    private String street;

    private Byte isDefaultAddress;

    private Date createDate;

    private Date updateDate;

    private Byte isValid;

    private Date invalidDate;
}
