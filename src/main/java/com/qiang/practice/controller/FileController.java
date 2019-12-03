package com.qiang.practice.controller;

import com.qiang.practice.service.FileService;
import com.qiang.practice.utils.response.ImageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "上传文件")
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 上传图片，通过io流写进硬盘
     * @param imageArray
     * @return
     */
    @ApiOperation("上传图片，通过io流写进硬盘")
    @RequestMapping(value = "/api/image/upload", method = RequestMethod.POST)
    public ImageResult uploadImageByIO(@RequestParam("image") MultipartFile[] imageArray) {

        return fileService.uploadImageByIO(imageArray);
    }

    /**
     * 加载图片，通过io流将硬盘上的图片推给浏览器
     * @param response
     * @param imageName
     */
    @ApiOperation("加载图片，通过io流将硬盘上的图片推给浏览器")
    @RequestMapping(value = "/api/image", method = RequestMethod.GET)
    public void loadImageByIO(HttpServletResponse response,
                              @RequestParam String imageName) {
        fileService.loadImageByIO(response, imageName);
    }

    /**
     * 下载文件
     * @param fileName 全局唯一文件名
     * @param downloadName  客户端想要展示的文件名
     */
    @RequestMapping(value = "/api/file", method = RequestMethod.GET)
    public void downloadFileByIO(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam String fileName,
                                 @RequestParam String downloadName) {
        fileService.downloadFileByIO(request, response, fileName, downloadName);
    }

}
