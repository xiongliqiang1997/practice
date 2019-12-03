package com.qiang.practice.service;

import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.utils.FileUtil;
import com.qiang.practice.utils.ImageUtil;
import com.qiang.practice.utils.UUIDUtil;
import com.qiang.practice.utils.response.ImageInfo;
import com.qiang.practice.utils.response.ImageResult;
import com.qiang.practice.utils.response.RUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("all")
@Transactional
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private ServerConfig serverConfig;

    @Override
    public ImageResult uploadImageByIO(MultipartFile[] imageArray) {
        try {
            long start = System.currentTimeMillis();
            if (imageArray.length == 0) {
                return RUtil.uploadImageEmpty();
            } else {
                String[] imagePathArray = new String[imageArray.length];
                List<ImageInfo> resImageInfoList = new ArrayList<>();
                int index = 0;

                File tempImage = null;
                tempImage = new File(serverConfig.getSaveFilePath());
                if (Objects.requireNonNull(tempImage.listFiles()).length > 500) {
                    serverConfig.setSonOneSaveFileFolder(serverConfig.getSonOneSaveFileFolder() + 1);
                    serverConfig.setSonTwoSaveFileFolder(0);
                    serverConfig.setSaveFileSonFullPath(
                            serverConfig.getDesOneFileFolderName(serverConfig.getSonOneSaveFileFolder())
                                    + File.separator
                                    + serverConfig.getDesTwoFileFolderName(serverConfig.getSonTwoSaveFileFolder()));
                    serverConfig.setSaveFilePath(serverConfig.getSaveFileParentPath()
                            + serverConfig.getSaveFileSonFullPath());

                    if (!new File(serverConfig.getSaveFilePath()).exists()) {
                        serverConfig.createNewSaveFile(serverConfig.getSonOneSaveFileFolder()
                                , serverConfig.getSonTwoSaveFileFolder());
                    }
                }

                boolean hasEmptyFlag = false;
                for (MultipartFile image : imageArray) {
                    //若图片为空，则跳过，并给客户端提示
                    if (image.isEmpty()) {
                        hasEmptyFlag = true;
                        continue;
                    }

                    String imageName = image.getOriginalFilename();
                    String imageExt = imageName.substring(imageName.lastIndexOf("."));
                    String serverFileName = UUIDUtil.getUUID();
                    String serverSavePath = serverConfig.getSaveFileSonFullPath() + serverFileName;

                    //将图片临时保存到本地，以便后续压缩和判断宽高使用
                    File file = new File(serverConfig.getSaveFileParentPath() + serverSavePath + "-origin" + imageExt);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    image.transferTo(file);

                    //压缩
                    serverSavePath = ImageUtil.compressPicForScale(
                            file,
                            serverConfig.getSaveFileParentPath() + serverSavePath,
                            serverConfig.getSaveFileSonFullPath() + serverFileName,
                            50L
                    );

                    //返回值
                    System.out.println(serverSavePath);
                    imagePathArray[index] = serverSavePath;
                    resImageInfoList.add(new ImageInfo(serverFileName, serverSavePath));
                    index++;

                    //删除临时文件
                    file.delete();
                }
                System.out.println("耗时约：" + (System.currentTimeMillis() - start) + "ms");
                return RUtil.uploadImageOk(imagePathArray, resImageInfoList, serverConfig.getImageUrl(imagePathArray[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return RUtil.uploadImageError();
        }
    }

    @Override
    public void loadImageByIO(HttpServletResponse response, String imageName) {
        InputStream in = null;
        OutputStream out = null;
        try {
            File tempImage = new File(serverConfig.getSaveFileParentPath() + imageName);
            if (!tempImage.exists()) { //文件不存在
                return;
            }
            response.setContentType(FileUtil.judgeImageType(tempImage.getName()));
            in = new FileInputStream(tempImage);
            out = new BufferedOutputStream(response.getOutputStream());
            byte[] bytes = new byte[1024];
            int n;
            while ((n = in.read(bytes)) != -1) {
                out.write(bytes, 0, n);
            }
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeOutputStream(out);
            FileUtil.closeInputStream(in);
        }
    }

    @Override
    public void downloadFileByIO(HttpServletRequest request, HttpServletResponse response,
                                 String fileName, String downloadName) {
        InputStream in = null;
        OutputStream out = null;
        File tempImage = null;
        try {
            tempImage = new File(serverConfig.getSaveFileParentPath() + fileName);
            if (!tempImage.exists()) { //文件不存在
                return;
            }
            //告知浏览器，是文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            //设置下载的文件名，并能正常显示中文
            FileUtil.setFileDownloadHeader(request, response, downloadName);
            in = new FileInputStream(tempImage);
            out = new BufferedOutputStream(response.getOutputStream());
            byte[] bytes = new byte[1024];
            int n;
            while ((n = in.read(bytes)) != -1) {
                out.write(bytes, 0, n);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeOutputStream(out);
            FileUtil.closeInputStream(in);
        }
    }
}
