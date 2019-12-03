package com.qiang.practice.utils;

import com.qiang.practice.exception.IllegalException;
import com.qiang.practice.model.FileInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @Author: CLQ
 * @Date: 2019/9/10
 * @Description: 文件处理
 */
public class FileUtil {
    public static String judgeImageType(String imageName) {
        String imageExt = imageName.substring(imageName.lastIndexOf(".") + 1);
        //判断图片格式,设置相应的输出文件格式
        if (imageExt.equals("jpg") || imageExt.equals("JPG") || imageExt.equals("jpeg")
                || imageExt.equals("JPEG") || imageExt.equals("jfif") || imageExt.equals("jpe")) {
            return "image/jpeg";
        } else if (imageExt.equals("png") || imageExt.equals("PNG")) {
            return "image/png";
        } else if (imageExt.equals("gif") || imageExt.equals("GIF")) {
            return "image/gif";
        } else if (imageExt.equals("tif") || imageExt.equals("TIF") || imageExt.equals("tiff")) {
            return "image/tiff";
        } else if (imageExt.equals("fax") || imageExt.equals("FAX")) {
            return "image/fax";
        } else if (imageExt.equals("ico") || imageExt.equals("ICO")) {
            return "image/x-icon";
        } else if (imageExt.equals("net")) {
            return "image/pnetvue";
        } else if (imageExt.equals("rp")) {
            return "image/vnd.rn-realpix";
        } else if (imageExt.equals("wbmp")) {
            return "image/vnd.wap.wbmp";
        } else {
            throw new IllegalException("图片类型不支持");
        }
    }

    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeOutputStream(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeInputStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeHSSFWorkbook(HSSFWorkbook hssfWorkbook) {
        if (hssfWorkbook != null) {
            try {
                hssfWorkbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeFileChannel(FileChannel fc) {
        if (fc != null) {
            try {
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeBufferedReader(BufferedReader br) {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeInputStreamReader(InputStreamReader isr) {
        if (isr != null) {
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeByteArrayToFile(String filePath, byte[] bytes) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
            os.write(bytes, 0, bytes.length);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeOutputStream(os);
        }
    }

    /**
     * 把一个文件转化为byte字节数组。
     *
     * @return
     */
    public static byte[] fileConvertToByteArray(File file) {
        byte[] data = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            data = baos.toByteArray();

            fis.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public static String getMsg(FileInfo fileInfo, String fileServerSonPath, String originFileServerSonPath) {
        if (ImageUtil.isImage(fileInfo.getFileType())) { //图片消息
            return originFileServerSonPath.concat("|").concat(fileServerSonPath).concat("|").concat(fileInfo.getContentMD5());
        } else { //文件消息
            return fileInfo.getFileName().concat("|").concat(fileServerSonPath).concat("|").concat(fileInfo.getContentMD5());
        }
    }

    public static String getFileType(String fileType) {
        return fileType.contains("image") ? "image" : "file";
    }

    /**
     * 根据不同浏览器设置下载文件名
     *
     * @param request
     * @param response
     * @param fileName
     */
    public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
        final String userAgent = request.getHeader("User-Agent");
        try {
            String finalFileName;
            if (StringUtils.containsAny(userAgent, "Firefox", "Chrome")) { //google,火狐浏览器
                finalFileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            } else { //其他浏览器
                finalFileName = URLEncoder.encode(fileName, "UTF8");
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + finalFileName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
