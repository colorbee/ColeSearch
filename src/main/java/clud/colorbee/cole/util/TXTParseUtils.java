//package clud.colorbee.cole.util;
//
//import java.io.*;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author heh
// * @date 2021/1/26
// */
//
//public class TXTParseUtils {
//
//
//    public void main(String[] args) {
//        /* 读取数据 */
//        try {
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("SiteURL.txt")),
//                    "UTF-8"));
//            String lineTxt = null;
//            while ((lineTxt = br.readLine()) != null) {//<br>　　　　　　　　　　//数据以逗号分隔
//                if (!lineTxt.contains("#commentBox")) {
//                    System.out.println(lineTxt);
//                }
//            }
//            br.close();
//        } catch (Exception e) {
//            System.err.println("read errors :" + e);
//        }
//
////        /* 输出数据 */
////        try {
////            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("E:/value_map.txt")),
////                    "UTF-8"));
////
////            for (String name : map.keySet()) {
////                bw.write(name + " " + map.get(name));
////                bw.newLine();
////            }
////            bw.close();
////        } catch (Exception e) {
////            System.err.println("write errors :" + e);
////        }
//    }
//}
