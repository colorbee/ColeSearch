package clud.colorbee.cole.controller;

import clud.colorbee.cole.common.JsonResult;
import clud.colorbee.cole.pojo.Article;
import clud.colorbee.cole.service.ISearchService;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.hunter.entity.VirtualArticle;
import me.zhyd.hunter.processor.BlogHunterProcessor;
import me.zhyd.hunter.processor.HunterProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author heh
 * @date 2021/1/26
 */
@Slf4j
@RestController
@RequestMapping
public class SpiderController {

    @Autowired
    private ISearchService searchService;

    @GetMapping("spider")
    public JsonResult getInfo(@RequestParam("url") String url) {
        boolean convertImage = true;
        HunterProcessor hunter = new BlogHunterProcessor(url, convertImage);
        CopyOnWriteArrayList<VirtualArticle> list = hunter.execute();
        list.forEach(virtualArticle -> {
            Article article = new Article();
            BeanUtils.copyProperties(virtualArticle, article);
            if (!article.isNull()) {
                searchService.addArticle(article);
                log.info("爬取成功---->{}", article.getTitle());
            }
        });
        return JsonResult.success("网址收录成功！",true);
    }

//    @GetMapping("spider/all")
//    public JsonResult all() {
//        new WebCrawler().myPrint("https://www.cnblogs.com/");
//        return JsonResult.success(true);
//    }

    @GetMapping("spider/txt")
    public JsonResult txt() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("SiteURL.txt")),
                    "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {//<br>　　　　　　　　　　//数据以逗号分隔
                if (!lineTxt.contains("#commentBox")) {
                    getInfo(lineTxt);
                }
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
        return JsonResult.success(true);
    }

//    public static void main(String[] args) {
//        WebCrawlerDemo webCrawlerDemo = new WebCrawlerDemo();
//        webCrawlerDemo.myPrint("https://blog.csdn.net");
//    }


}
