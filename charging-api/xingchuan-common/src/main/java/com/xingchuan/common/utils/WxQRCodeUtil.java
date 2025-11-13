package com.xingchuan.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.core.domain.model.QRCodeParam;
import com.xingchuan.common.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 二维码工具
 **/
public class WxQRCodeUtil {
    private static final Logger log = LoggerFactory.getLogger(WxQRCodeUtil.class);

    //CODE_WIDTH：二维码宽度，单位像素
    private static final int CODE_WIDTH = 400;
    //CODE_HEIGHT：二维码高度，单位像素
    private static final int CODE_HEIGHT = 400;
    //FRONT_COLOR：二维码前景色，0x000000 表示黑色
    private static final int FRONT_COLOR = 0x000000;
    //BACKGROUND_COLOR：二维码背景色，0xFFFFFF 表示白色
    //演示用 16 进制表示，和前端页面 CSS 的取色是一样的，注意前后景颜色应该对比明显，如常见的黑白
    private static final int BACKGROUND_COLOR = 0xFFFFFF;

    private static final String defaultBaseDir = "./qrCodePath";

    public static String getDefaultBaseDir() {
        return defaultBaseDir;
    }

    public static void createCodeToFile(String content, File codeImgFileSaveDir, String fileName) {
        try {
            if (StringUtils.isBlank(content) || StringUtils.isBlank(fileName)) {
                return;
            }
            content = content.trim();
            if (codeImgFileSaveDir == null || codeImgFileSaveDir.isFile()) {
                //二维码图片存在目录为空，默认放在桌面...
                codeImgFileSaveDir = FileSystemView.getFileSystemView().getHomeDirectory();
            }
            if (!codeImgFileSaveDir.exists()) {
                //二维码图片存在目录不存在，开始创建...
                codeImgFileSaveDir.mkdirs();
            }

            //核心代码-生成二维码
            BufferedImage bufferedImage = getBufferedImage(content);

            File codeImgFile = new File(codeImgFileSaveDir, fileName);
            ImageIO.write(bufferedImage, "png", codeImgFile);

            log.info("二维码图片生成成功：" + codeImgFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码并输出到输出流, 通常用于输出到网页上进行显示，输出到网页与输出到磁盘上的文件中，区别在于最后一句 ImageIO.write
     *
     * @param content      ：二维码内容
     * @param outputStream ：输出流，比如 HttpServletResponse 的 getOutputStream
     */
    public static void createCodeToOutputStream(String content, OutputStream outputStream) {
        try {
            if (StringUtils.isBlank(content)) {
                return;
            }
            content = content.trim();
            //核心代码-生成二维码
            BufferedImage bufferedImage = getBufferedImage(content);

            //区别就是这一句，输出到输出流中，如果第三个参数是 File，则输出到文件中
            ImageIO.write(bufferedImage, "png", outputStream);

            log.info("二维码图片生成到输出流成功...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量生成二维码并输出到 ZIP 文件中
     *
     * @param codeParamList 内容
     * @param outputStream  输出流
     */
    public static void createZipOfQRCodes(List<QRCodeParam> codeParamList, OutputStream outputStream) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            for (QRCodeParam qrCodeParam : codeParamList) {
                String content = qrCodeParam.getContent();
                if (StringUtils.isBlank(content)) {
                    continue;
                }
                content = content.trim();

                // 生成二维码图片
                BufferedImage bufferedImage = getBufferedImage(content);
                // 将二维码图片添加到ZIP文件中
                ZipEntry zipEntry = new ZipEntry(qrCodeParam.getName());
                zipOutputStream.putNextEntry(zipEntry);
                ImageIO.write(bufferedImage, "png", zipOutputStream);
                zipOutputStream.closeEntry();

            }

            zipOutputStream.finish();
            System.out.println("二维码 ZIP 文件生成成功...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //核心代码-生成二维码
    private static BufferedImage getBufferedImage(String content) throws WriterException {

        //com.google.zxing.EncodeHintType：编码提示类型,枚举类型
        Map<EncodeHintType, Object> hints = new HashMap();

        //EncodeHintType.CHARACTER_SET：设置字符编码类型
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        //EncodeHintType.ERROR_CORRECTION：设置误差校正
        //ErrorCorrectionLevel：误差校正等级，L = ~7% correction、M = ~15% correction、Q = ~25% correction、H = ~30% correction
        //不设置时，默认为 L 等级，等级不一样，生成的图案不同，但扫描的结果是一样的
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

        //EncodeHintType.MARGIN：设置二维码边距，单位像素，值越小，二维码距离四周越近
        hints.put(EncodeHintType.MARGIN, 1);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, CODE_WIDTH, CODE_HEIGHT, hints);
        BufferedImage bufferedImage = new BufferedImage(CODE_WIDTH, CODE_HEIGHT, BufferedImage.TYPE_INT_BGR);
        for (int x = 0; x < CODE_WIDTH; x++) {
            for (int y = 0; y < CODE_HEIGHT; y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? FRONT_COLOR : BACKGROUND_COLOR);
            }
        }
        return bufferedImage;
    }

    /**
     * 生成微信小程序二维码
     *
     * @param appId        appId
     * @param appSecret    appSecret
     * @param page         跳转页面
     * @param sence        参数
     * @param outputStream outputStream
     */
    public static void generateWXQRCode(String appId, String appSecret, String page, String sence, OutputStream outputStream) {
        try {
            String path = page + "?" + sence;
            byte[] bytes = generateQRCode(appId, appSecret, path);
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);

            BufferedImage bufferedImage = null;
            bufferedImage = ImageIO.read(in);

            //区别就是这一句，输出到输出流中，如果第三个参数是 File，则输出到文件中
            ImageIO.write(bufferedImage, "png", outputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成小程序二维码数组
     */
    public static byte[] generateQRCode(String appId, String appSecret, String pagePath) {
        String accessToken = WxQRCodeUtil.getAccessToken(appId, appSecret);
        if (accessToken == null) {
            System.out.println("Failed to get Access Token.");
            return null;
        }

        try {
            String urlStr = "https://api.weixin.qq.com/wxa/getwxacode?access_token=%s&path=%s";
            URL url = new URL(String.format(urlStr, accessToken, pagePath));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            String postData = "{\"path\":\"" + pagePath + "\",\"width\":430}";
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(postDataBytes);
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, length);
                }
                inputStream.close();
                return byteArrayOutputStream.toByteArray();
            } else {
                System.out.println("HTTP request failed: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成普通二维码，带参数 方形
     *
     * @param appId
     * @param appSecret
     * @param page      跳转页面
     * @param scene     参数
     */
    public static void generateWXQRCodePT(String appId, String appSecret, String page, String scene, OutputStream outputStream) throws ServiceException {
        String accessToken = WxQRCodeUtil.getAccessToken(appId, appSecret);
        if (accessToken == null) {
            throw new ServiceException("微信小程序配置错误！");
        }
        try {
            URL url = new URL("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            // post请求必传以下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("content-Type", "application/json;charset=utf-8");

            // 发送请求参数
            JSONObject paramJson = new JSONObject();
            paramJson.put("scene", scene);
            paramJson.put("page", page);
            // 设置扫描二维码后跳转的页面
            paramJson.put("width", 430);
            paramJson.put("is_hyaline", true);
            paramJson.put("auto_color", true);
            String postData = paramJson.toString();
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);

            OutputStream outStream = httpURLConnection.getOutputStream();
            outStream.write(postDataBytes);
            outStream.flush();
            outStream.close();

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, length);
                }
                inputStream.close();

                if (byteArrayOutputStream.toString().contains("errcode")) {
                    throw new ServiceException(byteArrayOutputStream.toString());
                }

                ByteArrayInputStream in = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                BufferedImage bufferedImage = null;
                bufferedImage = ImageIO.read(in);

                ImageIO.write(bufferedImage, "png", outputStream);
            } else {
                throw new ServiceException("接口调用失败：" + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("生成二维码失败：" + e.getMessage());
        }
    }

    /**
     * 获取Access Token
     *
     * @param appId     appId
     * @param appSecret appSecret
     * @return Access Token
     */
    public static String getAccessToken(String appId, String appSecret) {
        // 构建请求URL
        String url = "https://api.weixin.qq.com/cgi-bin/stable_token";
        try {
            // 创建URL对象
            URL obj = new URL(url);
            // 创建HttpURLConnection对象
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // 设置请求方法为POST
            con.setRequestMethod("POST");
            // 添加请求头
            con.setRequestProperty("Content-Type", "application/json");
            // 构建请求体
            String requestBody = "{\"grant_type\": \"client_credential\",\"appid\": \"" + appId + "\", \"secret\": \"" + appSecret + "\"}";
            // 启用输出流
            con.setDoOutput(true);
            // 获取输出流并写入请求体数据
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            // 读取响应
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // 解析 JSON 响应，提取 Access Token
            JSONObject resultObject = JSON.parseObject(response.toString());
            if (resultObject.containsKey(MessageConstants.WECHAT_ERR_CODE)) {
                log.error("获取access_token失败");
            }
            return resultObject.getString("access_token");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}