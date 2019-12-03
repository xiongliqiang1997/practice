package com.qiang.practice.config;

import com.qiang.practice.base.Constants;
import com.qiang.practice.utils.FileUtil;
import com.qiang.practice.utils.IPUtils;
import com.qiang.practice.utils.SpringContextUtil;
import com.qiang.practice.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

/**
 * @Author: CLQ
 * @Date: 2019/7/5
 * @Description: 项目基本配置
 */
@Slf4j
@Component
public class ServerConfig implements ApplicationListener<ApplicationEvent> {

    //项目端口号
    private int serverPort;

    //存储文件的路径
    private String saveFilePath;
    private int sonOneSaveFileFolder;
    private int sonTwoSaveFileFolder;
    private String saveFileParentPath;
    private String saveFileSonFullPath;

    private final String webIp = getWebIp(); //本机外网ip
    private final String localAreaIp = IPUtils.getLocalIp4AddressStrFast(); //本机局域网ip

    @Autowired
    private WebSocketServer webSocketServer;

    @Value("${server.port}")
    private int realServerPort;

    /**
     * 获取项目URL前缀(如：http://xxx.xx.xx.xx:8080)
     *
     * @return
     */
    private String getServerUrl() {
        String ipAddress;
        if (SpringContextUtil.isLinuxOS(File.separator)) { //Linux,获取本机外网ip
//            ipAddress = webIp;
            ipAddress = "www.clqly.com";
        } else { //Windows,开发环境，获取本机局域网IP
            ipAddress = localAreaIp;
        }
        assert ipAddress != null;
        return "https://".concat(ipAddress).concat(":").concat(String.valueOf(this.serverPort));
    }

    /**
     * 根据图片名字获取加载图片的URL
     *
     * @param imageName
     * @return
     */
    public String getImageUrl(String imageName) {
        return getServerUrl().concat("/api/image?imageName=").concat(imageName);
    }

    /**
     * 根据文件名字获取下载文件url
     *
     * @param fileName
     * @return
     */
    public String getFileDownloadUrl(String fileName) {
        return getServerUrl().concat("/api/file/?fileName=").concat(fileName);
    }

    /**
     * 获取本机外网IP
     *
     * @return 外网IP
     */
    private static String getWebIp() {
        BufferedReader br = null;
        InputStreamReader isr = null;
        try {
            URL url = new URL(Constants.GET_WEB_IP_URL);
            isr = new InputStreamReader(url.openStream());
            br = new BufferedReader(isr);
            String webContent;
            StringBuilder sb = new StringBuilder();
            while ((webContent = br.readLine()) != null) {
                sb.append(webContent).append("\r\n");
            }
            br.close();
            webContent = sb.toString();
            log.info(webContent);
            return webContent.substring(
                    webContent.indexOf("\"cip\": \"") + 8,
                    webContent.indexOf("\", \"cid\"")
            );
        } catch (Exception e) {
            log.error("获取外网IP失败", e);
            return "error";
        } finally {
            FileUtil.closeBufferedReader(br);
            FileUtil.closeInputStreamReader(isr);
        }
    }

    /**
     * 在项目启动时获的项目所在端口号以及文件存储路径
     *
     * @param applicationEvent
     */
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof WebServerInitializedEvent) {

            WebServerInitializedEvent webServerInitializedEvent = (WebServerInitializedEvent) applicationEvent;

            serverPort = webServerInitializedEvent.getWebServer().getPort();
            if (realServerPort != serverPort) { //当启动Springboot-actuator监控程序时，不执行下面的代码
                return;
            }

            saveFileParentPath = getSaveImagePath(this.getClass()).concat(File.separator + "recieveFile" + File.separator);
            //jar包运行时，获取到的路径会以 file:\ 或 file:/   开头(Linux中分隔符为/)，截掉
            if (StringUtils.equals("file:" + File.separator, saveFileParentPath.substring(0, 6))) {
                if (SpringContextUtil.isLinuxOS(File.separator)) { //Linux
                    saveFileParentPath = saveFileParentPath.substring(5);
                } else { //Windows
                    saveFileParentPath = saveFileParentPath.substring(6);
                }
            }

            File recieveImageFile = new File(saveFileParentPath);

            //若该路径不存在，则创建
            if (!recieveImageFile.exists()) {

                if (recieveImageFile.mkdir()) {
                    //接收图片文件夹创建成功, 在它下面创建20*20个子文件夹(从0开始)
                    createNewSaveFile(0, 0);
                    sonOneSaveFileFolder = 0;
                    sonTwoSaveFileFolder = 0;
                    saveFileSonFullPath = getDesOneFileFolderName(sonOneSaveFileFolder)
                            + File.separator
                            + getDesTwoFileFolderName(sonTwoSaveFileFolder)
                            + File.separator;
                    saveFilePath = saveFileParentPath + saveFileSonFullPath;
                    log.info("接收图片文件夹创建成功, 文件位置: {}", saveFilePath);
                } else {
                    log.error("接收图片文件夹创建失败");
                }

            } else {
                File tempFile;
                boolean isEnough = false;
                //循环判断当前应该从哪个文件夹开始存储
                outer:
                for (int i = 0; i < Constants.MAX_ONE_FILE_FOLDER_NUM; i++) {

                    for (int j = 0; j < Constants.MAX_TWO_FILE_FOLDER_NUM; j++) {
                        tempFile = new File(saveFileParentPath
                                + getDesOneFileFolderName(i)
                                + File.separator
                                + getDesTwoFileFolderName(j));

                        if (Objects.requireNonNull(tempFile.listFiles()).length < 500) {
                            sonOneSaveFileFolder = i;
                            sonTwoSaveFileFolder = j;
                            saveFileSonFullPath = getDesOneFileFolderName(sonOneSaveFileFolder)
                                    + File.separator
                                    + getDesTwoFileFolderName(sonTwoSaveFileFolder)
                                    + File.separator;
                            saveFilePath = saveFileParentPath + saveFileSonFullPath;
                            log.info("当前接收图片文件夹位置为: {}", saveFilePath);
                            isEnough = true;
                            break outer;
                        }
                    }
                }

                if (!isEnough) { //当前文件夹已不足,再添加20*20个文件夹
                    int parentFileFolderStart = Objects.requireNonNull(new File(saveFileParentPath).listFiles()).length;
                    createNewSaveFile(parentFileFolderStart, 0);

                    sonOneSaveFileFolder = parentFileFolderStart;
                    sonTwoSaveFileFolder = 0;
                    saveFileSonFullPath = getDesOneFileFolderName(sonOneSaveFileFolder)
                            + File.separator
                            + getDesTwoFileFolderName(sonTwoSaveFileFolder)
                            + File.separator;
                    saveFilePath = saveFileParentPath + saveFileSonFullPath;
                    log.info("创建了一批新的文件夹，当前接收图片文件夹位置为: {}", saveFilePath);
                }
            }
        } else if (applicationEvent instanceof ContextClosedEvent && realServerPort == serverPort) {
            webSocketServer.getSocketCache().values().forEach(webSocketSession -> {
                //设置所有用户在线状态为离线
                webSocketServer.doAfterWebSocketClose(webSocketSession.getAttributes(), false);
            });
            webSocketServer.setIsServletStop(true);
        }
    }

    /**
     * 父级文件夹从 parentFileFolderStart 开始
     * 子级文件夹从 sonFileFolderStart 开始
     * 创建 20*20个文件夹，用来存储文件
     *
     * @param parentFileFolderStart
     * @param sonFileFolderStart
     */
    public void createNewSaveFile(int parentFileFolderStart, int sonFileFolderStart) {
        File tempFile;
        for (int i = parentFileFolderStart; i < parentFileFolderStart + Constants.MAX_ONE_FILE_FOLDER_NUM; i++) {
            for (int j = sonFileFolderStart; j < sonFileFolderStart + Constants.MAX_TWO_FILE_FOLDER_NUM; j++) {
                tempFile = new File(saveFileParentPath
                        + getDesOneFileFolderName(i)
                        + File.separator
                        + getDesTwoFileFolderName(j));
                tempFile.mkdirs();
            }
        }
    }

    /**
     * 获取存储图片文件的路径
     *
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> String getSaveImagePath(Class<T> clazz) {
        File file = new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath());
        if (StringUtils.contains(file.getPath(), File.separator + "target" + File.separator + "classes")) { //IDE中启动
            return file.getParent();
        } else { //jar包或war包启动
            return file.getParentFile().getParentFile().getParent();
        }
    }

    public String getDesOneFileFolderName(int oneFileFolderNum) {
        return String.format("%0" + String.valueOf(Constants.MAX_ONE_FILE_FOLDER_NUM).length() + "d", oneFileFolderNum);
    }

    public String getDesTwoFileFolderName(int twoFileFolderNum) {
        return String.format("%0" + String.valueOf(Constants.MAX_TWO_FILE_FOLDER_NUM).length() + "d", twoFileFolderNum);
    }

    public String getSaveFilePath() {
        return this.saveFilePath;
    }

    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    public int getSonOneSaveFileFolder() {
        return sonOneSaveFileFolder;
    }

    public void setSonOneSaveFileFolder(int sonOneSaveFileFolder) {
        this.sonOneSaveFileFolder = sonOneSaveFileFolder;
    }

    public int getSonTwoSaveFileFolder() {
        return sonTwoSaveFileFolder;
    }

    public void setSonTwoSaveFileFolder(int sonTwoSaveFileFolder) {
        this.sonTwoSaveFileFolder = sonTwoSaveFileFolder;
    }

    public String getSaveFileParentPath() {
        return saveFileParentPath;
    }

    public String getSaveFileSonFullPath() {
        return saveFileSonFullPath;
    }

    public void setSaveFileSonFullPath(String saveFileSonFullPath) {
        this.saveFileSonFullPath = saveFileSonFullPath;
    }
}
