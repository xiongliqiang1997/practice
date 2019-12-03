package com.qiang.practice.model;

import lombok.Data;

import java.io.File;
import java.util.Date;

/**
 * @Author: CLQ
 * @Date: 2019/9/9
 * @Description: 待上传文件基本信息类
 */
@Data
public class FileInfo {
    private Long id; //数据库中该消息的id
    private Date createDate; //数据库中该消息的创建时间
    private boolean isSysUser; //是否是用户相关模块
    private String fileName; //客户端文件原名
    private String fileServerPath; //服务器路径
    private String fileServerName; //服务器文件名
    private String fileUrl; //缩略图加载url
    private String originUrl; //原图url
    private String toUserId; //消息接收方id
    private String fileServerSonPath; //
    private String originFileServerSonPath;
    private Long fileSize;
    private String fileType;
    private Long separateSize;
    private int percent;
    private Long indexStart;
    private long indexEnd;
    private int index;
    private String sign;
    private String contentMD5;

    public void fileUploadComplete() {
        if (fileServerPath != null && this.indexStart < fileSize) {
            File serverFile = new File(fileServerPath);
            if (serverFile.exists()) {
                if (serverFile.delete()) {
                    System.out.println("文件删除成功, 服务器文件名: " + fileServerName + ", 原文件名: " + fileName);
                }
            }
        }
    }

    public boolean getIsSysUser() {
        return isSysUser;
    }

    public void setIsSysUser(boolean isSysUser) {
        this.isSysUser = isSysUser;
    }
}
