package clud.colorbee.cole.controller;

import clud.colorbee.cole.common.JsonResult;
import clud.colorbee.cole.service.ISolveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author heh
 * @date 2021/1/26
 */
@Slf4j
@RestController
@RequestMapping("/solve")
public class SolveController {

    private final ISolveService solveService;

    public SolveController(ISolveService solveService) {
        this.solveService = solveService;
    }

    @GetMapping
    public JsonResult solve(@RequestParam("id") String id) {
        return JsonResult.success(solveService.articleDetailStatic(id));
    }
}
