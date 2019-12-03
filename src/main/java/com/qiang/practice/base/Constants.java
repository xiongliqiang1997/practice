package com.qiang.practice.base;

/**
 * @Author: CLQ
 * @Date: 2019/7/18
 * @Description: 常量类
 */
public class Constants {

    /**
     * redis中组织机构树形菜单的key
     */
    public static final String TREE_MENU_JSON = "treeMenuJson";
    public static final String TREE_MENU_JSON_MY = "myTreeMenuJson";

    /**
     * redis中商品分类树形菜单的key
     */
    public static final String PRODUCT_TYPE_TREE_MENU_JSON = "productTypeTreeMenuJson";

    /**
     * 请求头中token的key
     */
    public final static String TOKEN_HEADER_PARAM = "X-Authorization";

    /**
     * 请求头中区分官网还是后台的key
     */
    public final static String TYPE_HEADER_PARAM = "type";

    /**
     * 代表官网的type的值
     */
    public final static String TYPE_WEB_PARAM = "web";

    /**
     * 付款多长时间后发货 单位：分钟
     */
    public static final int SEND_PRODUCT_AFTER_PAY = 1;

    /**
     * 发货多长时间后订单完成 单位：分钟
     */
    public static final int FINISH_ORDER_AFTER_SEND = 2;

    /**
     * 未付款多长时间后订单取消 单位：分钟
     */
    public static final int CANCELLED_ORDER_AFTER_CREATE = 3;

    /**
     * 用户订单：待付款状态的码值
     */
    public static final Byte USER_ORDER_WAIT_PAY_STATUS = 0;

    /**
     * 用户订单：已付款状态的码值
     */
    public static final Byte USER_ORDER_HAS_PAYED_STATUS = 1;

    /**
     * 订单已发货状态
     */
    public static final Byte USER_ORDER_HAS_SEND_STATUS = 2;
    /**
     * 订单已完成状态
     */
    public static final Byte USER_ORDER_HAS_FINISHED_STATUS = 3;
    /**
     * 订单已取消状态
     */
    public static final Byte USER_ORDER_HAS_CANCELLED_STATUS = 4;
    /**
     * 物流信息中已出库状态
     */
    public static final Byte LOGISTICS_HAS_OUT_OF_STOCK_STATUS = 1;
    /**
     * 物流信息中已签收状态
     */
    public static final Byte LOGISTICS_HAS_SIGNED_STATUS = 2;
    /**
     * 用户密码加密时的盐值
     */
    public static final String LOGIN_PWD_SLAT = "&%5123**#*&&%%$$#@pwdqwerhahaha--%%";
    /**
     * token加密时的盐值
     */
    public static final String JWT_TOKEN_SECRET = "&%5123**#*&&%%$$#@";
    /**
     * 商品库存在redis中的key
     */
    public static final String PRODUCT_STOCK = "product_stock";
    /**
     * redis中存储  用户正在与谁聊天  的key
     */
    public static final String USER_CHAT_WITH_WHO = "user_chat_with_who";

    /**
     * 获取本机外网ip的网站地址
     */
    public static final String GET_WEB_IP_URL = "http://pv.sohu.com/cityjson?ie=utf-8";

    /**
     * 已经登录用户，在redis的hash的标识
     */
    public static final String HAS_LOGIN_USER_LIST = "has_login_user_list";

    /**
     * 存储文件的第一层文件夹数量
     */
    public static final int MAX_ONE_FILE_FOLDER_NUM = 20;
    /**
     * 存储文件的第二层文件夹数量
     */
    public static final int MAX_TWO_FILE_FOLDER_NUM = 20;
    public static final String SAVE_FILE_MD5_MAP = "save_file_md5_map";
    public static final String SAVE_FILE_MD5_USE_COUNT = "save_file_md5_use_count";
    public static final String USER_LOGIN_INFO_MAP_LIST = "user_logined_map_list";
    public static final String LOGIN_TOKEN = "practice:login_token:";
}
