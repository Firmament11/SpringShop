package org.example.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.springboot.common.Result;
import org.example.springboot.entity.Product;
import org.example.springboot.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品管理接口")
@RestController
@RequestMapping("/product")
public class ProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Operation(summary = "创建商品")
    @PostMapping
    public Result<?> createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @Operation(summary = "更新商品信息")
    @PutMapping("/{id}")
    public Result<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/{id}")
    public Result<?> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @Operation(summary = "根据ID获取商品详情")
    @GetMapping("/{id}")
    public Result<?> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    

    @Operation(summary = "分页查询商品列表")
    @GetMapping("/page")
    public Result<?> getProductsByPage(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long farmerId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return Result.success(productService.getProductsByPage(name, categoryId, farmerId, status,
                currentPage, size, sortField, sortOrder, minPrice, maxPrice));
    }

    @Operation(summary = "更新商品状态")
    @PutMapping("/{id}/status")
    public Result<?> updateProductStatus(@PathVariable Long id, @RequestParam Integer status) {
        return productService.updateProductStatus(id, status);
    }

    @Operation(summary = "批量删除商品")
    @DeleteMapping("/batch")
    public Result<?> deleteBatch(@RequestParam List<Long> ids) {
        return productService.deleteBatch(ids);
    }
    // 获取全部商品
    @GetMapping("/all")
    public Result<?> getAllProducts() {
        return Result.success(productService.getProductsByPage(null, null, null, null, 1, Integer.MAX_VALUE, null, null, null, null).getRecords());
    }

    @Operation(summary = "批量更新商品状态")
    @PutMapping("/batch/status")
    public Result<?> updateBatchStatus(
            @RequestParam List<Long> ids,
            @RequestParam Integer status) {
        return productService.updateBatchStatus(ids, status);
    }

} 
