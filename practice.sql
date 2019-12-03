/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : practice

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 03/12/2019 15:55:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for org_achievement
-- ----------------------------
DROP TABLE IF EXISTS `org_achievement`;
CREATE TABLE `org_achievement`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user` bigint(20) NOT NULL COMMENT '创建人',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `sub_title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '副标题',
  `event_date` datetime(0) NOT NULL COMMENT '事件日期',
  `is_publish` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否发布 1发布 0不发布 默认1',
  `img_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '缩略图地址',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '事件内容',
  `create_date` datetime(0) NOT NULL COMMENT '创建日期',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `is_valid` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效 1有效 0无效 默认1',
  `invalid_date` datetime(0) NULL DEFAULT NULL COMMENT '无效日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_impression
-- ----------------------------
DROP TABLE IF EXISTS `org_impression`;
CREATE TABLE `org_impression`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user` bigint(20) NOT NULL COMMENT '创建人',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `sub_title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '副标题',
  `event_date` datetime(0) NOT NULL COMMENT '事件日期',
  `is_publish` tinyint(4) NOT NULL COMMENT '是否发布',
  `img_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '缩略图地址',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '事件内容',
  `create_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `is_valid` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `invalid_date` datetime(0) NULL DEFAULT NULL COMMENT '无效日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '企业印象' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_profile
-- ----------------------------
DROP TABLE IF EXISTS `org_profile`;
CREATE TABLE `org_profile`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '简介内容',
  `sys_user` bigint(20) NOT NULL COMMENT '最后修订人',
  `revision_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最后修订日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '学校简介' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of org_profile
-- ----------------------------
INSERT INTO `org_profile` VALUES (64, '', 52, '2019-10-18 18:36:26');

-- ----------------------------
-- Table structure for org_scenery
-- ----------------------------
DROP TABLE IF EXISTS `org_scenery`;
CREATE TABLE `org_scenery`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user` bigint(20) NOT NULL COMMENT '创建人',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `img_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片路径',
  `orderby` int(11) NOT NULL COMMENT '排序',
  `create_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `is_valid` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效1有效0无效，默认1',
  `invalid_date` datetime(0) NULL DEFAULT NULL COMMENT '无效日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '企业风光' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of org_scenery
-- ----------------------------
INSERT INTO `org_scenery` VALUES (48, 54, '测试', '00/00/6c623cf42f024e4581e34e906fe16209.jpg', 1, '2019-11-27 17:31:21', NULL, 1, NULL);

-- ----------------------------
-- Table structure for plate
-- ----------------------------
DROP TABLE IF EXISTS `plate`;
CREATE TABLE `plate`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plate_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '板块名称',
  `is_use` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `plate_icon` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '板块图标',
  `plate_img` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '板块背景',
  `orderby` int(11) NOT NULL COMMENT '排序',
  `create_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `is_valid` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `invalid_date` datetime(0) NULL DEFAULT NULL COMMENT '无效日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '板块管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for plate_info
-- ----------------------------
DROP TABLE IF EXISTS `plate_info`;
CREATE TABLE `plate_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plate` bigint(20) NOT NULL COMMENT '所属板块',
  `sys_user` bigint(20) NOT NULL COMMENT '创建人',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `sub_title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '副标题',
  `is_top` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否置顶1是0否默认0',
  `is_publish` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否发布1是0否默认1',
  `org_impression` tinyint(4) NOT NULL DEFAULT 0 COMMENT '企业印象1是0否默认0',
  `img_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缩略图',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `publish_date` datetime(0) NULL DEFAULT NULL COMMENT '发布日期',
  `read_num` int(11) NOT NULL COMMENT '阅读数量',
  `create_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `is_valid` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `invalid_date` datetime(0) NULL DEFAULT NULL COMMENT '无效日期',
  `event_date` datetime(0) NULL DEFAULT NULL COMMENT '事件日期',
  `org_achievement` tinyint(4) NOT NULL DEFAULT 0 COMMENT '企业成果1是0否默认0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 110 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '板块信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for primary_member
-- ----------------------------
DROP TABLE IF EXISTS `primary_member`;
CREATE TABLE `primary_member`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user` bigint(20) NOT NULL COMMENT '创建人',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '出生日期',
  `work_date` datetime(0) NULL DEFAULT NULL COMMENT '参加工作日期',
  `nation` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '民族',
  `politic_countenance` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '政治面貌',
  `title` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职称',
  `remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '简介',
  `orderby` int(11) NOT NULL COMMENT '排序',
  `img_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '缩略图路径',
  `create_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `is_valid` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `invalid_date` datetime(0) NULL DEFAULT NULL COMMENT '无效日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '主要成员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_type` bigint(20) NOT NULL COMMENT '所属分类',
  `sys_user` bigint(20) NOT NULL COMMENT '创建人',
  `product_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `product_price` decimal(10, 2) NOT NULL COMMENT '价格',
  `remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '描述',
  `is_publish` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否发布1是0否默认1',
  `publish_date` datetime(0) NULL DEFAULT NULL COMMENT '发布日期',
  `view_num` int(11) NOT NULL COMMENT '浏览数量',
  `create_date` datetime(0) NOT NULL COMMENT '创建日期',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `is_valid` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效1是0否默认1',
  `invalid_date` datetime(0) NULL DEFAULT NULL COMMENT '失效日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product_img
-- ----------------------------
DROP TABLE IF EXISTS `product_img`;
CREATE TABLE `product_img`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL COMMENT '商品id',
  `img_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品图片地址',
  `orderby` tinyint(4) NOT NULL COMMENT '排序',
  `create_date` datetime(0) NOT NULL COMMENT '创建时间',
  `is_valid` tinyint(4) NOT NULL COMMENT '是否有效',
  `invalid_date` datetime(0) NULL DEFAULT NULL COMMENT '失效日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 237 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product_inbound_record
-- ----------------------------
DROP TABLE IF EXISTS `product_inbound_record`;
CREATE TABLE `product_inbound_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user` bigint(20) NOT NULL COMMENT '入库人',
  `product_source` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '货源',
  `create_date` datetime(0) NOT NULL COMMENT '入库时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product_inbound_record_detail
-- ----------------------------
DROP TABLE IF EXISTS `product_inbound_record_detail`;
CREATE TABLE `product_inbound_record_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_inbound_record_id` bigint(20) NOT NULL COMMENT '入库记录id',
  `product_id` bigint(20) NOT NULL COMMENT '入库商品id',
  `num` int(11) NOT NULL COMMENT '入库数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product_type
-- ----------------------------
DROP TABLE IF EXISTS `product_type`;
CREATE TABLE `product_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `p_id` bigint(20) NOT NULL COMMENT '上级分类id',
  `type_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分类名称',
  `orderby` int(11) NOT NULL COMMENT '排序',
  `create_date` datetime(0) NOT NULL COMMENT '创建日期',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `is_valid` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `invalid_date` datetime(0) NULL DEFAULT NULL COMMENT '失效日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for read_log
-- ----------------------------
DROP TABLE IF EXISTS `read_log`;
CREATE TABLE `read_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user` bigint(20) NOT NULL COMMENT '阅读人',
  `plate_info` bigint(20) NOT NULL COMMENT '阅读的信息',
  `read_time` datetime(0) NULL DEFAULT NULL COMMENT '阅读时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 421 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_area
-- ----------------------------
DROP TABLE IF EXISTS `sys_area`;
CREATE TABLE `sys_area`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `p_id` int(11) NOT NULL COMMENT '父级地区id',
  `area_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地区名称',
  `area_type` tinyint(2) NOT NULL COMMENT '地区类型 0-无 1-省 2-市 3-县/区',
  `create_date` datetime(0) NOT NULL COMMENT '创建时间',
  `is_valid` tinyint(2) NOT NULL DEFAULT 1 COMMENT '是否有效 0-无效 1-有效 默认1',
  `invalid_date` datetime(0) NULL DEFAULT NULL COMMENT '失效时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_area
-- ----------------------------
INSERT INTO `sys_area` VALUES (1, 0, '北京市', 1, '2019-08-26 11:09:06', 1, NULL);
INSERT INTO `sys_area` VALUES (2, 1, '北京市', 2, '2019-08-26 11:09:24', 1, NULL);
INSERT INTO `sys_area` VALUES (3, 2, '昌平区', 3, '2019-08-26 11:09:51', 1, NULL);
INSERT INTO `sys_area` VALUES (4, 2, '朝阳区', 3, '2019-08-26 11:10:08', 1, NULL);
INSERT INTO `sys_area` VALUES (5, 2, '海淀区', 3, '2019-08-26 11:10:19', 1, NULL);
INSERT INTO `sys_area` VALUES (6, 2, '东城区', 3, '2019-08-26 11:10:38', 1, NULL);
INSERT INTO `sys_area` VALUES (7, 0, '河北省', 1, '2019-08-26 11:11:01', 1, NULL);
INSERT INTO `sys_area` VALUES (8, 7, '石家庄市', 2, '2019-08-26 11:11:16', 1, NULL);
INSERT INTO `sys_area` VALUES (9, 8, '新华区', 3, '2019-08-26 11:12:07', 1, NULL);
INSERT INTO `sys_area` VALUES (10, 8, '无极县', 3, '2019-08-26 11:12:38', 1, NULL);
INSERT INTO `sys_area` VALUES (11, 8, '赵县', 3, '2019-08-26 11:12:45', 1, NULL);
INSERT INTO `sys_area` VALUES (12, 8, '高邑县', 3, '2019-08-26 11:13:17', 1, NULL);
INSERT INTO `sys_area` VALUES (13, 7, '邯郸市', 2, '2019-08-26 11:14:19', 1, NULL);
INSERT INTO `sys_area` VALUES (14, 13, '邱县', 3, '2019-08-26 13:05:43', 1, NULL);
INSERT INTO `sys_area` VALUES (15, 13, '魏县', 3, '2019-08-26 13:06:01', 1, NULL);
INSERT INTO `sys_area` VALUES (16, 13, '涉县', 3, '2019-08-26 13:06:20', 1, NULL);
INSERT INTO `sys_area` VALUES (17, 13, '临漳县', 3, '2019-08-26 13:06:31', 1, NULL);
INSERT INTO `sys_area` VALUES (18, 13, '邯郸县', 3, '2019-08-26 13:06:42', 1, NULL);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user` bigint(20) NOT NULL COMMENT '操作人',
  `log_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作类型',
  `log_result` tinyint(4) NOT NULL DEFAULT 1 COMMENT '操作结果1成功0失败',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '日志内容',
  `create_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 157 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日志管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_msg_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_msg_type`;
CREATE TABLE `sys_msg_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_msg_type
-- ----------------------------
INSERT INTO `sys_msg_type` VALUES (1, 'text');
INSERT INTO `sys_msg_type` VALUES (2, 'image');
INSERT INTO `sys_msg_type` VALUES (3, 'file');

-- ----------------------------
-- Table structure for sys_org
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `p_id` bigint(20) NULL DEFAULT NULL COMMENT '上级机构',
  `org_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构名称',
  `remark` varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构简介',
  `orderby` int(11) NOT NULL COMMENT '排序',
  `create_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `is_valid` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效1是0否默认1',
  `invalid_date` datetime(0) NULL DEFAULT NULL COMMENT '无效日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 235 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组织机构' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_org
-- ----------------------------
INSERT INTO `sys_org` VALUES (1, 0, '用户', '用户', 1, '2019-10-21 15:44:28', '2019-10-21 15:44:28', 1, '2019-10-21 15:44:46');

-- ----------------------------
-- Table structure for sys_tourist_msg
-- ----------------------------
DROP TABLE IF EXISTS `sys_tourist_msg`;
CREATE TABLE `sys_tourist_msg`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_user_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '鍙戦€佷汉',
  `to_user_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '鎺ユ敹浜?',
  `msg` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `msg_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息类型( 文字消息:text；图片消息:image；文件消息:file )',
  `is_recall` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否已撤回 0未撤回 1已撤回 默认0',
  `is_read` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否已读 0未读 1已读 默认0',
  `del_user_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除了该消息的用户id',
  `create_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '发送时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 273 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_tourist_msg_list
-- ----------------------------
DROP TABLE IF EXISTS `sys_tourist_msg_list`;
CREATE TABLE `sys_tourist_msg_list`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '游客或者客服的id',
  `another_user_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '他的聊天列表中另一个人的id',
  `last_msg_date` datetime(0) NULL DEFAULT NULL COMMENT '最后一次聊天的时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3544 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `org_id` bigint(20) NOT NULL COMMENT '所属机构',
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `user_sex` tinyint(4) NOT NULL DEFAULT -1 COMMENT '性别-1保密0男1女',
  `login_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录名',
  `login_pwd` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `phone` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `img_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户头像url',
  `is_customer_service` tinyint(2) NOT NULL COMMENT '是否是客服',
  `have_authority` tinyint(4) NOT NULL DEFAULT 0 COMMENT '后台权限0无权限1有权限默认0',
  `orderby` int(11) NOT NULL COMMENT '排序',
  `create_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `is_valid` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `invalid_date` datetime(0) NULL DEFAULT NULL COMMENT '无效日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (71, 1, '管理员', -1, 'admin', 'e2cb7d321726b6a0264166ea8d976d4f', '18833842723', '00/00/117cca9b8af8424d8aef0d8f10a9827f.jpg', 1, 1, 1, '2019-10-21 17:02:06', '2019-10-21 17:02:31', 0, '2019-11-20 16:09:18');

-- ----------------------------
-- Table structure for sys_user_msg
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_msg`;
CREATE TABLE `sys_user_msg`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_user_id` bigint(20) NOT NULL COMMENT '发送人',
  `to_user_id` bigint(20) NOT NULL COMMENT '接收人',
  `msg` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `msg_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息类型( 文字消息:text；图片消息:image；文件消息:file )',
  `is_recall` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否已撤回 0未撤回 1已撤回 默认0',
  `is_read` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否已读 0未读 1已读 默认0',
  `del_user_id` bigint(20) NULL DEFAULT NULL COMMENT '删除了该消息的用户id',
  `create_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '发送时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 260 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_msg_list
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_msg_list`;
CREATE TABLE `sys_user_msg_list`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user` bigint(20) NOT NULL COMMENT '用户消息列表id',
  `another_user_id` bigint(20) NOT NULL COMMENT '好友id',
  `last_msg_date` datetime(0) NULL DEFAULT NULL COMMENT '最后一次聊天的时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_status
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_status`;
CREATE TABLE `sys_user_status`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user` bigint(20) NOT NULL COMMENT '用户id',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '在线状态 0离线  1在线 默认0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_status
-- ----------------------------
INSERT INTO `sys_user_status` VALUES (1, 71, 0);

-- ----------------------------
-- Table structure for user_address
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user` bigint(20) NOT NULL COMMENT '所属用户',
  `consignee_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人姓名',
  `consignee_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人电话',
  `province` int(11) NOT NULL COMMENT '省',
  `city` int(11) NOT NULL COMMENT '市',
  `area` int(11) NOT NULL COMMENT '区',
  `street` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '详细地址',
  `is_default_address` tinyint(4) NOT NULL COMMENT '是否默认地址',
  `create_date` datetime(0) NOT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `is_valid` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效 1有效 0无效 默认1',
  `invalid_date` datetime(0) NULL DEFAULT NULL COMMENT '失效日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_order
-- ----------------------------
DROP TABLE IF EXISTS `user_order`;
CREATE TABLE `user_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
  `sys_user` bigint(20) NOT NULL COMMENT '创建人',
  `status` tinyint(4) NOT NULL COMMENT '订单状态（0待付款1已付款2已发货3已完成4已取消）',
  `amount_total` decimal(10, 2) NULL DEFAULT NULL COMMENT '实付总价',
  `address_id` bigint(20) NOT NULL COMMENT '收货信息id',
  `pay_channel` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付渠道',
  `pay_no` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付编号',
  `create_date` datetime(0) NOT NULL COMMENT '创建时间',
  `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '付款时间',
  `is_valid` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效 1有效 0无效 默认1',
  `invalid_date` datetime(0) NULL DEFAULT NULL COMMENT '失效日期',
  `consignee_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人姓名',
  `consignee_phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人电话',
  `consignee_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 141 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_order_logistics
-- ----------------------------
DROP TABLE IF EXISTS `user_order_logistics`;
CREATE TABLE `user_order_logistics`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '物流编号',
  `user_order_product_id` bigint(20) NOT NULL COMMENT '订单商品id',
  `logistics_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '快递公司',
  `fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '运费',
  `real_pay` decimal(10, 2) NULL DEFAULT NULL COMMENT '实际支付金额',
  `status` tinyint(4) NOT NULL COMMENT '物流状态 0无物流 1已发货  2已签收',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 92 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_order_product
-- ----------------------------
DROP TABLE IF EXISTS `user_order_product`;
CREATE TABLE `user_order_product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_order_id` bigint(20) NOT NULL COMMENT '订单id',
  `product_id` bigint(20) NOT NULL COMMENT '商品id',
  `first_product_img_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品第一张图片地址',
  `product_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `num` int(11) NOT NULL COMMENT '商品数量',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '买家备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 228 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `user_shopping_cart`;
CREATE TABLE `user_shopping_cart`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user` bigint(20) NOT NULL COMMENT '所属用户',
  `product_id` bigint(20) NOT NULL COMMENT '商品id',
  `num` int(11) NOT NULL COMMENT '数量',
  `create_date` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 291 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
