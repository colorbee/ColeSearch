package clud.colorbee.cole.util;

import me.zhyd.hunter.config.HunterConfig;
import me.zhyd.hunter.config.HunterConfigContext;
import me.zhyd.hunter.config.platform.Platform;
import me.zhyd.hunter.entity.VirtualArticle;
import me.zhyd.hunter.enums.ExitWayEnum;
import me.zhyd.hunter.processor.BlogHunterProcessor;
import me.zhyd.hunter.processor.HunterProcessor;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author heh
 * @date 2021/1/26
 */
public class Reptile {
    public CopyOnWriteArrayList<VirtualArticle> imooc() {
        HunterConfig config = HunterConfigContext.getHunterConfig(Platform.IMOOC);
        // set会重置，add会追加
        config.setEntryUrls("https://www.imooc.com/u/1175248/articles")
                .addEntryUrl("https://www.imooc.com/u/4321686/articles")
                // 设置程序退出的方式
                .setExitWay(ExitWayEnum.URL_COUNT)
                // 设定抓取120秒， 如果所有文章都被抓取过了，则会提前停止
                .setCount(2)
                // 每次抓取间隔的时间
                .setSleepTime(100)
                // 失败重试次数
                .setRetryTimes(3)
                // 针对抓取失败的链接 循环重试次数
                .setCycleRetryTimes(3)
                // 开启的线程数
                .setThreadCount(5)
                // 开启图片转存
                .setConvertImg(false);
        HunterProcessor hunter = new BlogHunterProcessor(config);
        CopyOnWriteArrayList<VirtualArticle> list = hunter.execute();
        if (null == list || list.isEmpty()) {
            System.out.println("没获取到数据");
        }
        return list;
    }

    public CopyOnWriteArrayList<VirtualArticle> csdn() {
        HunterConfig config = HunterConfigContext.getHunterConfig(Platform.IMOOC);
        // set会重置，add会追加
        config.setEntryUrls("https://www.imooc.com/u/1175248/articles")
                .addEntryUrl("https://www.imooc.com/u/4321686/articles")
                // 设置程序退出的方式
                .setExitWay(ExitWayEnum.URL_COUNT)
                // 设定抓取120秒， 如果所有文章都被抓取过了，则会提前停止
                .setCount(2)
                // 每次抓取间隔的时间
                .setSleepTime(100)
                // 失败重试次数
                .setRetryTimes(3)
                // 针对抓取失败的链接 循环重试次数
                .setCycleRetryTimes(3)
                // 开启的线程数
                .setThreadCount(5)
                // 开启图片转存
                .setConvertImg(false);
        HunterProcessor hunter = new BlogHunterProcessor(config);
        CopyOnWriteArrayList<VirtualArticle> list = hunter.execute();
        if (null == list || list.isEmpty()) {
            System.out.println("没获取到数据");
        }
        return list;
    }

}
