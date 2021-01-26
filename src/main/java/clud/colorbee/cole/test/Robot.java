package clud.colorbee.cole.test;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * java实现爬虫
 */
public class Robot {
    public static void main1(String[] args) {
        System.out.println("https://www.cnblogs.com/alangur/p/12965908.html".indexOf("html"));

    }

    public static void main(String[] args) {
        URL url = null;
        URLConnection urlconn = null;
        BufferedReader br = null;
        PrintWriter pw = null;
//        String regex = "http://[\\w+\\.?/?]+\\.[A-Za-z]+";
        String regex = "https://[\\w+\\.?/?]+\\.[A-Za-z]+";//url匹配规则
        Pattern p = Pattern.compile(regex);
        try {
            url = new URL("https://www.cnblogs.com/cate/108701/#p2");//爬取的网址、这里爬取的是一个生物网站
            urlconn = url.openConnection();
            Set set = new HashSet();
            pw = new PrintWriter(new FileWriter("SiteURL.txt"), true);//将爬取到的链接放到D盘的SiteURL文件中
            br = new BufferedReader(new InputStreamReader(
                    urlconn.getInputStream()));
            String buf = null;
            while ((buf = br.readLine()) != null) {
                Matcher buf_m = p.matcher(buf);
                while (buf_m.find()) {
                    if(buf_m.group().indexOf("html")>0){
                        set.add(buf_m.group());
                    }
                    pw.println(buf_m.group());
                }
            }
            System.out.println("爬取成功^_^");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pw.close();


        }
    }
}