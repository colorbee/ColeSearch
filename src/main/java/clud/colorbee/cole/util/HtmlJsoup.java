package clud.colorbee.cole.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author heh
 * @date 2021/1/26
 */
public class HtmlJsoup {

    private static final String BASE64_PATTEN = "^data:image/(png|gif|jpg|jpeg|bmp|tif|psd|ICO);base64,.*";
    private static final String NOT_IMG_PATTEN = "^.*[.](png|gif|jpg|jpeg|bmp|tif|psd|ICO)$";

    /**
     * 检测图片路径是否为真实有效的路径方法
     *
     * @param src
     * @return
     */
    public static boolean checkImage(String src) {


        //使用正则表达式，排除img标签src属性值已经是base64编码的情况
        Pattern pattern = Pattern.compile(BASE64_PATTEN);
        Matcher matcher = pattern.matcher(src);
        if (matcher.matches()) {
            return false;
        }
        //排除src路径并非图片格式的情况
        pattern = Pattern.compile(NOT_IMG_PATTEN);
        matcher = pattern.matcher(src);
        if (!matcher.matches()) {
            return false;
        }
        //排除图片路径不存在的情况
//        String path = "D:\\tmp\\a.html";
//        File file = new File(path);
//        if(!file.exists())
//            return false;

        return true;
    }

    public static String htmlImgToBase64(String html) {
        Document doc = Jsoup.parse(html, "utf-8");
        Elements imgs = doc.getElementsByTag("img");

        for (Element img : imgs) {
            String src = img.attr("src");

            if (!checkImage(src)) {
                continue;
            }
            //将有效的路径传入base64编码的方法
            img.attr("src", ImageBase64.imageToBase64Head(src));

        }

        //返回base64编码后的html文档
        return doc.getElementsByTag("body").html();
    }
}
