package com.qiang.practice.utils;

import com.qiang.practice.exception.IllegalException;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: CLQ
 * @Date: 2019/9/19
 * @Description: TODO
 */
@SuppressWarnings("all")
public class ImageUtil {
    private static Logger log = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * @param srcPath     原图片地址
     * @param desPath     目标图片地址
     * @param desFileSize 指定图片大小,单位kb
     * @return
     */
    public static String compressPicForScale(String srcPath, String desPath,
                                             String srcFileName, String desFileName,
                                             long desFileSize) {
        try {
            float outputQuality = 1f;
            File srcFile = new File(srcPath);

            //大小合适不压缩
            if (srcFile.length() < desFileSize * 1024) {
                log.info("【图片大小: " + srcFile.length() / 1024 + "kb, 没有进行压缩】");
                return srcFileName;
            } else if (srcFile.length() < desFileSize * 1024 * 2) {
                outputQuality = 0.6f;
            } else {
                outputQuality = desFileSize * 1024f * 2 / srcFile.length();
            }

            //当图片超过目标大小, 进行压缩处理
            log.info("原图片:" + srcPath + ", 大小:" + srcFile.length() / 1024 + "kb");

            //压缩
            Thumbnails.of(srcPath).scale(getScale(srcFile, 1200, 900))
                    .outputQuality(outputQuality)
                    .outputFormat("jpg").toFile(desPath);

            //打印压缩后图片大小, 方便调试
            File desFile = new File(desPath.concat(".jpg"));
            log.info("目标图片:" + desFileName + ", 大小:" + desFile.length() / 1024 + "kb");
            log.info("图片压缩完成!");
            return desFileName.concat(".jpg");
        } catch (Exception e) {
            log.error("【图片压缩失败!】", e);
            return srcFileName;
        }
    }

    /**
     * 根据指定大小压缩图片
     *
     * @param imageBytes  源图片字节数组
     * @param desFileSize 指定图片大小，单位kb
     * @param desPath     目标图片地址
     * @return 压缩质量后的图片字节数组
     */
    public static String compressPicForScale(File srcFile, String desPath,
                                             String desFileName, long desFileSize) throws IOException {
        try {

            long srcFileSize = srcFile.length();

            //大小合适, 直接保存
            if (srcFileSize < desFileSize * 1024) {
                log.info("【图片大小："+ srcFileSize / 1024 +"kb, 没有进行压缩】");
                Thumbnails.of(srcFile).scale(1.0).outputFormat("jpg").toFile(desPath);
                return desFileName.concat(".jpg");
            }

            //图片超过目标大小, 进行压缩处理
            Thumbnails.of(srcFile).scale(getScale(srcFile, 1200, 900))
                    .outputQuality((desFileSize * 1024d) / srcFileSize)
                    .outputFormat("jpg").toFile(desPath);

            desPath = desPath.concat(".jpg");
            //打印压缩后图片大小, 方便调试
            File desFile = new File(desPath);
            log.info("【图片压缩】图片原大小="+ srcFileSize +"kb | 压缩后大小="+ desFile.length() / 1024 +"kb");
            log.info("压缩完成");
            return desFileName.concat(".jpg");
        } catch (Exception e) {
            log.error("【图片压缩失败!】", e);
            throw new IllegalException("服务器异常, 图片上传失败");
        }
    }

    /**
     * 根据图片源文件获取图片尺寸压缩比
     * @param srcFile
     * @param desWidth
     * @param desHeight
     * @return
     * @throws IOException
     */
    public static double getScale(File srcFile, int desWidth, int desHeight) throws IOException {
        //计算宽高
        return getScale(ImageIO.read(srcFile), desWidth, desHeight);
    }

    /**
     * 根据图片源文件流获取图片尺寸压缩比
     * @param srcInputStream
     * @param desWidth
     * @param desHeight
     * @return
     * @throws IOException
     */
    public static double getScale(InputStream srcInputStream, int desWidth, int desHeight) throws IOException {
        //计算宽高
        return getScale(ImageIO.read(srcInputStream), desWidth, desHeight);
    }

    /**
     * 根据图片对象获取图片尺寸压缩比
     * @param srcBim
     * @param desWidth
     * @param desHeight
     * @return
     */
    public static double getScale(BufferedImage srcBim, int desWidth, int desHeight) {
        double scale = 1.0;
        int imgWidth = srcBim.getWidth();
        int imgHeight = srcBim.getHeight();
        //处理图片像素宽度（当宽度像素大于1200时进行图片等比缩放宽度）
        while (imgWidth * scale > desWidth) {
            scale -= 0.025;
        }
        while (imgHeight * scale > desHeight) {//当高度像素仍大于900时继续缩放
            scale -= 0.02;
        }
        return scale;
    }

    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < 900) {
            accuracy = 0.85;
        } else if (size < 2047) {
            accuracy = 0.6;
        } else if (size < 3275) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }

    /**
     * 根据文件后缀名判断是否是否是图片
     *
     * @param fileNameExt
     * @return
     */
    public static boolean isImage(String fileType) {
        return StringUtils.contains(fileType, "image");
    }

    /**
     * 根据文件后缀名判断是否是否是文件
     *
     * @param fileNameExt
     * @return
     */
    public static boolean isNotText(String fileType) {
        return StringUtils.containsNone(fileType, "file");
    }

    /**
     * 处理最后一条消息的显示样式
     * 若消息为缩略图url+原图url，则显示 [图片]
     * 若消息为文件名+文件url，则显示 [文件]
     * @param lastMsg
     * @return
     */
    public static String dealLastMsg(String lastMsg) {
        if (StringUtils.contains(lastMsg, "|")) {
            if (StringUtils.contains(lastMsg.substring(0, lastMsg.indexOf("|")), File.separator)) {
                //图片
                return "[图片]";
            } else { //文件
                return "[文件]";
            }
        } else {
            return lastMsg;
        }
    }
}
