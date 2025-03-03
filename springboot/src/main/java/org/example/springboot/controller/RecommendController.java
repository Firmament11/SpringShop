package org.example.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.springboot.common.Result;
import org.example.springboot.service.RecommendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "推荐管理接口")
@RestController
@RequestMapping("/recommend")
public class RecommendController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendController.class);

    @Autowired
    private RecommendService recommendService;

    @Operation(summary = "获取用户推荐")
    @GetMapping("/user/{userId}")
    public Result<?> getRecommendations(@PathVariable Long userId) {
        return recommendService.generateRecommendations(userId);
    }

    @Operation(summary = "更新所有用户推荐")
    @PostMapping("/update")
    public Result<?> updateRecommendations() {
        recommendService.updateRecommendations();
        return Result.success();
    }
} 