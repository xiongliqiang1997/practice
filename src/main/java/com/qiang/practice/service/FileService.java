package com.qiang.practice.service;

import com.qiang.practice.utils.response.ImageResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FileService {

    /**
     * 上传图片，通过io将图片写入硬盘
     * @param imageArray
     * @return
     */
    ImageResult uploadImageByIO(MultipartFile[] imageArray);

    /**
     * 根据图片名拿到文件，通过io流将图片退给浏览器
     * @param response
     * @param imageName
     */
    void loadImageByIO(HttpServletResponse response, String imageName);

    /**
     * 下载文件
     * @param fileName 全局唯一文件名
     * @param downloadName  客户端想要展示的文件名
     */
    void downloadFileByIO(HttpServletRequest request, HttpServletResponse response, String fileName, String downloadName);
}
