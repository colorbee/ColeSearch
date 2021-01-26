package clud.colorbee.cole.util;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import clud.colorbee.cole.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author heh
 * @date 2021/1/26
 */
@Slf4j
public class ImageBase64 {

    /**
     * 将图片转换成Base64编码 ,带头文件
     * @param imgFile 待处理图片
     * @return
     */
    public static String imageToBase64Head(String imgFile){
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String type = imgFile.substring(imgFile.length()-3,imgFile.length());
        //为编码添加头文件字符串
        String head = "data:image/"+type+";base64,";

        String path = imgFile;
        if(!imgFile.contains("https://") && !imgFile.contains("http://")){
            path =  "http:" + imgFile;
        }
        String baseUrl = imgBase64(path);
        if("fail".equals(baseUrl)){
            //todo 换成服务器url
            return "http://10.0.0.14:99/img/404.jpg";
//            return "http://10.0.0.35:9933/img/404.jpg";
        }
        return head + baseUrl;
    }

    public static String imgBase64(String imgURL) {
        log.info("处理imageURL：{}",imgURL);
        ByteArrayOutputStream outPut = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        InputStream inStream = null;
        try {
            // 创建URL
//            URL url = new URL(imgURL);
            URL url= new URL(null, imgURL, new sun.net.www.protocol.https.Handler());
            // 创建链接
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3970.5 Safari/537.36");
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);

            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                //连接失败/链接失效/图片不存在
                return "fail";
            }
            inStream = conn.getInputStream();
            int len = -1;
            while ((len = inStream.read(data)) != -1) {
                outPut.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(500100,e.getMessage());
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new BusinessException(500100,e.getMessage());
                }
            }
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(outPut.toByteArray());
    }

    public static void main(String[] args) {
        String s = imgBase64("https://syyu.top/usr/uploads/2021/01/3347312617.png");
        System.out.println(s);
    }
}
