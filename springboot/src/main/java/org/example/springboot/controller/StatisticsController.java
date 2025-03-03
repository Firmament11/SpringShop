package org.example.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.springboot.common.Result;
import org.example.springboot.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "统计分析接口")
@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private StatisticsService statisticsService;

    @Operation(summary = "获取本月订单统计")
    @GetMapping("/orders/monthly")
    public Result<?> getMonthlyOrderStatistics(@RequestParam(required = false) Long farmerId) {
        LOGGER.info("获取本月订单统计, farmerId: {}", farmerId);
        Map<String, Object> statistics = statisticsService.getMonthlyOrderStatistics(farmerId);
        return Result.success(statistics);
    }

    @Operation(summary = "获取本月销售额统计")
    @GetMapping("/sales/monthly")
    public Result<?> getMonthlySalesStatistics(@RequestParam(required = false) Long farmerId) {
        LOGGER.info("获取本月销售额统计, farmerId: {}", farmerId);
        Map<String, Object> statistics = statisticsService.getMonthlySalesStatistics(farmerId);
        return Result.success(statistics);
    }

    @Operation(summary = "获取用户订单统计")
    @GetMapping("/user/orders")
    public Result<?> getUserOrderStatistics(@RequestParam Long userId) {
        LOGGER.info("获取用户订单统计, userId: {}", userId);
        Map<String, Object> statistics = statisticsService.getUserOrderStatistics(userId);
        return Result.success(statistics);
    }

    @Operation(summary = "获取用户消费统计")
    @GetMapping("/user/spending")
    public Result<?> getUserSpendingStatistics(@RequestParam Long userId) {
        LOGGER.info("获取用户消费统计, userId: {}", userId);
        Map<String, Object> statistics = statisticsService.getUserSpendingStatistics(userId);
        return Result.success(statistics);
    }

    @Operation(summary = "获取用户总数统计")
    @GetMapping("/users/yearly")
    public Result<?> getYearlyUserStatistics() {
        LOGGER.info("获取年度用户统计");
        Map<String, Object> statistics = statisticsService.getYearlyUserStatistics();
        return Result.success(statistics);
    }

    @Operation(summary = "获取热销商品Top5")
    @GetMapping("/products/top5")
    public Result<?> getTopSellingProducts() {
        LOGGER.info("获取热销商品Top5统计");
        Map<String, Object> statistics = statisticsService.getTopSellingProducts();
        return Result.success(statistics);
    }

    @Operation(summary = "获取品类销售占比")
    @GetMapping("/category/sales")
    public Result<?> getCategorySalesStatistics() {
        LOGGER.info("获取品类销售占比统计");
        Map<String, Object> statistics = statisticsService.getCategorySalesStatistics();
        return Result.success(statistics);
    }
} 