package org.example.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.springboot.common.Result;
import org.example.springboot.entity.Cart;
import org.example.springboot.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@Tag(name = "购物车管理接口")
@RestController
@RequestMapping("/cart")
public class CartController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);
    private static final String IMAGE_STORAGE_PATH = "./files/img/";
    @Autowired
    private CartService cartService;

    @Operation(summary = "添加商品到购物车")
    @PostMapping
    public Result<?> addToCart(@RequestBody Cart cart) {
        return cartService.addToCart(cart);
    }

    @Operation(summary = "更新购物车商品数量")
    @PutMapping("/{id}")
    public Result<?> updateCartItem(@PathVariable Long id, @RequestParam Integer quantity) {
        return cartService.updateCartItem(id, quantity);
    }

    @Operation(summary = "删除购物车商品")
    @DeleteMapping("/{id}")
    public Result<?> deleteCartItem(@PathVariable Long id) {
        return cartService.deleteCartItem(id);
    }

    @Operation(summary = "根据用户ID获取购物车")
    @GetMapping("/user/{userId}")
    public Result<?> getCartByUserId(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @Operation(summary = "清空购物车")
    @DeleteMapping("/clear/{userId}")
    public Result<?> clearCart(@PathVariable Long userId) {
        return cartService.clearCart(userId);
    }

    @Operation(summary = "分页查询购物车")
    @GetMapping("/page")
    public Result<?> getCartByPage(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String productName,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size) {
        return cartService.getCartByPage(userId, productName, currentPage, size);
    }
    @Operation(summary = "批量删除购物车项")
    @DeleteMapping("/batch")
    public Result<?> deleteBatch(@RequestParam List<Long> ids) {
        return cartService.deleteBatch(ids);
    }
} 