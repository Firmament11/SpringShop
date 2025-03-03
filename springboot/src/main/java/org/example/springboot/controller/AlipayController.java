package org.example.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.example.springboot.common.Result;
import org.example.springboot.service.AlipayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author 86186
 */
@Tag(name = "支付宝支付接口")
@RestController
@RequestMapping("/alipay")
public class AlipayController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlipayController.class);

    @Autowired
    private AlipayService alipayService;

    @Operation(summary = "创建支付")
    @GetMapping("/pay/{orderId}")
    public void pay(@PathVariable Long orderId, HttpServletResponse response) throws Exception {
        LOGGER.info("收到支付创建请求，订单ID：{}", orderId);
        alipayService.pay(orderId, response);
    }

    @Operation(summary = "支付通知回调")
    @PostMapping("/notify")
    public void paymentNotify(HttpServletRequest request) throws Exception {
        alipayService.handlePaymentNotify(request);
    }
} 