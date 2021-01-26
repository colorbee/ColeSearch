package clud.colorbee.cole.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author heh
 * @date 2021/1/26
 */
@Controller
public class ViewController {

    @GetMapping
    public String show(){
        return "show";
    }

    @GetMapping("/to/search")
    public ModelAndView toSearch(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("search");
        return modelAndView;
    }

    @GetMapping("/article")
    public ModelAndView toArticle(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("article");
        return modelAndView;
    }
}
