package org.example.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.springboot.common.Result;
import org.example.springboot.entity.Address;
import org.example.springboot.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "地址管理接口")
@RestController
@RequestMapping("/address")
public class AddressController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private AddressService addressService;

    @Operation(summary = "创建地址")
    @PostMapping
    public Result<?> createAddress(@RequestBody Address address) {
        return addressService.createAddress(address);
    }

    @Operation(summary = "更新地址信息")
    @PutMapping("/{id}")
    public Result<?> updateAddress(@PathVariable Long id, @RequestBody Address address) {
        return addressService.updateAddress(id, address);
    }

    @Operation(summary = "删除地址")
    @DeleteMapping("/{id}")
    public Result<?> deleteAddress(@PathVariable Long id) {
        return addressService.deleteAddress(id);
    }

    @Operation(summary = "根据ID获取地址详情")
    @GetMapping("/{id}")
    public Result<?> getAddressById(@PathVariable Long id) {
        return addressService.getAddressById(id);
    }

    @Operation(summary = "根据用户ID获取地址列表")
    @GetMapping("/user/{userId}")
    public Result<?> getAddressesByUserId(@PathVariable Long userId) {
        return addressService.getAddressesByUserId(userId);
    }

    @Operation(summary = "分页查询地址列表")
    @GetMapping("/page")
    public Result<?> getAddressesByPage(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size) {
        return addressService.getAddressesByPage(userId, currentPage, size);
    }

    @Operation(summary = "批量删除地址")
    @DeleteMapping("/batch")
    public Result<?> deleteBatch(@RequestParam List<Long> ids) {
        return addressService.deleteBatch(ids);
    }
} 