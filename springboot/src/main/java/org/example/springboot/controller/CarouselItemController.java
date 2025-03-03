package org.example.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.springboot.common.Result;
import org.example.springboot.entity.CarouselItem;
import org.example.springboot.service.CarouselItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "轮播图管理接口")
@RestController
@RequestMapping("/carousel")
public class CarouselItemController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarouselItemController.class);

    @Autowired
    private CarouselItemService carouselItemService;

    @Operation(summary = "创建轮播图")
    @PostMapping
    public Result<?> createCarouselItem(@RequestBody CarouselItem carouselItem) {
        return carouselItemService.createCarouselItem(carouselItem);
    }

    @Operation(summary = "更新轮播图")
    @PutMapping("/{id}")
    public Result<?> updateCarouselItem(@PathVariable Long id, @RequestBody CarouselItem carouselItem) {
        return carouselItemService.updateCarouselItem(id, carouselItem);
    }

    @Operation(summary = "删除轮播图")
    @DeleteMapping("/{id}")
    public Result<?> deleteCarouselItem(@PathVariable Long id) {
        return carouselItemService.deleteCarouselItem(id);
    }

    @Operation(summary = "获取轮播图详情")
    @GetMapping("/{id}")
    public Result<?> getCarouselItemById(@PathVariable Long id) {
        return carouselItemService.getCarouselItemById(id);
    }

    @Operation(summary = "获取所有启用的轮播图")
    @GetMapping("/active")
    public Result<?> getActiveCarouselItems() {
        return carouselItemService.getActiveCarouselItems();
    }

    @Operation(summary = "分页获取轮播图列表")
    @GetMapping("/page")
    public Result<?> getCarouselItemsByPage(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size) {
        return carouselItemService.getCarouselItemsByPage(currentPage, size);
    }
} 