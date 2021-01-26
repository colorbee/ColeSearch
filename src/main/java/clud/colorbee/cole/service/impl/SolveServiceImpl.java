package clud.colorbee.cole.service.impl;

import clud.colorbee.cole.service.ISearchService;
import clud.colorbee.cole.util.HtmlJsoup;
import clud.colorbee.cole.pojo.vo.ArticleDetailVO;
import clud.colorbee.cole.service.ISolveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

/**
 *
 * @author heh
 * @date 2021/1/26
 */
@Service
@Slf4j
public class SolveServiceImpl implements ISolveService {

    private final TemplateEngine templateEngine;
    private final ISearchService searchService;

    @Value("${solve.detail.destPath}")
    private String destPath;

    public SolveServiceImpl(ISearchService searchService, TemplateEngine templateEngine) {
        this.searchService = searchService;
        this.templateEngine = templateEngine;
    }

    @Override
    public String articleDetailStatic(String id) {
        ArticleDetailVO articleDetailVO = searchService.getById(id);

        boolean exists = new File(destPath, articleDetailVO.getKeyId() + ".html").exists();

        if (exists) {
            return articleDetailVO.getKeyId().toString();
        }
        //构造上下文(Model)
        log.info("处理url：{}中...",id);
        String baseHtml = HtmlJsoup.htmlImgToBase64(articleDetailVO.getContent());
        log.info("处理url：{}完毕...",id);
        Context context = new Context();
        context.setVariable("title", articleDetailVO.getTitle());
        context.setVariable("content", baseHtml);


        File file = new File(destPath);
        File originFile = new File(file, articleDetailVO.getKeyId() + ".html");

        try (PrintWriter write = new PrintWriter(originFile, "utf-8")) {
            log.info("渲染url：{}中...",id);
            templateEngine.process("staticPage", context, write);
            log.info("渲染url：{}完毕...",id);
        } catch (IOException e) {
            log.info(MessageFormat.format("页面静态化失败，失败原因：{0}", e.getMessage()));
            e.printStackTrace();
        }
        return articleDetailVO.getKeyId().toString();
    }
}
