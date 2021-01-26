package clud.colorbee.cole.controller;

import clud.colorbee.cole.common.JsonResult;
import clud.colorbee.cole.service.ISearchService;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author heh
 * @date 2021/1/26
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    private final ISearchService searchService;

    public SearchController(ISearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public JsonResult keywordSearch(@RequestParam("keyword") String keyword,
                                    @RequestParam("pageIndex") Integer pageIndex,
                                    @RequestParam("pageSize") Integer pageSize) {
        return JsonResult.success(searchService.keySearch(pageIndex,pageSize,keyword));
    }

    @GetMapping("/auto")
    public JsonResult autoComplete(@RequestParam("keyword") String keyword) {
        return JsonResult.success(searchService.autoComplete(keyword));
    }

    @GetMapping("/getById")
    public JsonResult getById(@RequestParam("id") String id) {
        return JsonResult.success(searchService.getById(id));
    }
}
