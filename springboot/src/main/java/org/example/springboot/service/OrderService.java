package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.springboot.common.Result;
import org.example.springboot.entity.Order;
import org.example.springboot.entity.Product;
import org.example.springboot.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;





    public Result<?> createOrder(Order order) {
        try {
            // 检查商品库存
            Product product = productMapper.selectById(order.getProductId());
            if (product == null) {
                return Result.error("-1", "商品不存在");
            }
            if (product.getStock() < order.getQuantity()) {
                return Result.error("-1", "库存不足");
            }

            // 计算总价
            order.setTotalPrice(order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())));

            int result = orderMapper.insert(order);
            if (result > 0) {
                // 更新商品库存
                product.setStock(product.getStock() - order.getQuantity());
                productMapper.updateById(product);

                LOGGER.info("创建订单成功，订单ID：{}", order.getId());
                return Result.success(order);
            }
            return Result.error("-1", "创建订单失败");
        } catch (Exception e) {
            LOGGER.error("创建订单失败：{}", e.getMessage());
            return Result.error("-1", "创建订单失败：" + e.getMessage());
        }
    }

    public Result<?> updateOrderStatus(Long id, Integer status) {
        try {
            Order order = orderMapper.selectById(id);
            if (order == null) {
                return Result.error("-1", "未找到订单");
            }

            order.setLastStatus(order.getStatus());
            order.setStatus(status);
            int result = orderMapper.updateById(order);
            if (result > 0) {
                LOGGER.info("更新订单状态成功，订单ID：{}，新状态：{}", id, status);
                return Result.success(order);
            }
            return Result.error("-1", "更新订单状态失败");
        } catch (Exception e) {
            LOGGER.error("更新订单状态失败：{}", e.getMessage());
            return Result.error("-1", "更新订单状态失败：" + e.getMessage());
        }
    }

    public Result<?> deleteOrder(Long id) {
        try {

            int result = orderMapper.deleteById(id);
            if (result > 0) {
                LOGGER.info("删除订单成功，订单ID：{}", id);
                return Result.success();
            }
            return Result.error("-1", "删除订单失败");
        } catch (Exception e) {
            LOGGER.error("删除订单失败：{}", e.getMessage());
            return Result.error("-1", "删除订单失败：" + e.getMessage());
        }
    }

    public Result<?> getOrderById(Long id) {
        Order order = orderMapper.selectById(id);
        if (order != null) {
            // 填充关联信息
            order.setUser(userMapper.selectById(order.getUserId()));
            order.setProduct(productMapper.selectById(order.getProductId()));

            return Result.success(order);
        }
        return Result.error("-1", "未找到订单");
    }

    public Result<?> getOrdersByUserId(Long userId) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, userId);
        List<Order> orders = orderMapper.selectList(queryWrapper);
        if (orders != null && !orders.isEmpty()) {
            // 填充关联信息
            orders.forEach(order -> {
                order.setUser(userMapper.selectById(order.getUserId()));
                order.setProduct(productMapper.selectById(order.getProductId()));

            });
            return Result.success(orders);
        }
        return Result.error("-1", "未找到订单");
    }

    public Result<?> getOrdersByPage(Long userId,Long id,String status, Long farmerId,Integer currentPage, Integer size) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            queryWrapper.eq(Order::getUserId, userId);
        }
        if (id != null) {
            queryWrapper.eq(Order::getId, id);
        }
        if(StringUtils.isNotBlank(status)){
            queryWrapper.eq(Order::getStatus,status);
        }
        if (farmerId != null) {
            // order有productId， product表中 farmerId 与 farmer表中 id 关联
            List<Product> product = productMapper.selectList(new LambdaQueryWrapper<Product>().eq(Product::getFarmerId, farmerId));

            if(!product.isEmpty()){


            // 收集productId
            List<Long> productIds = product.stream().map(Product::getId).collect(Collectors.toList());
            queryWrapper.in(Order::getProductId, productIds);
            }else{
                Page<Order> page = new Page<>(currentPage, size);
                page.setTotal(0);
                page.setRecords(null);
                return Result.success(page);
            }

        }




        Page<Order> page = new Page<>(currentPage, size);
        Page<Order> result = orderMapper.selectPage(page, queryWrapper);

        // 填充关联信息
        result.getRecords().forEach(order -> {

            order.setUser(userMapper.selectById(order.getUserId()));
            Product product = productMapper.selectById(order.getProductId());
            if(product!=null){
                order.setProduct(product);
                order.setFarmer(userMapper.selectById(product.getFarmerId()));
            }else{
         
                order.setProduct(null);
                order.setFarmer(null);

            }





        });

        return Result.success(result);
    }

    public Result<?> refundOrder(Long id, String reason) {
        try {
            Order order = orderMapper.selectById(id);
            if (order == null) {
                return Result.error("-1", "未找到订单");
            }

            // 检查订单状态是否允许退款
            if (order.getStatus() != 1 && order.getStatus() != 2) {
                return Result.error("-1", "当前订单状态不允许退款");
            }

            order.setRefundStatus(1); // 申请退款
            order.setRefundReason(reason);
            int result = orderMapper.updateById(order);
            if (result > 0) {
                LOGGER.info("申请退款成功，订单ID：{}", id);
                return Result.success(order);
            }
            return Result.error("-1", "申请退款失败");
        } catch (Exception e) {
            LOGGER.error("申请退款失败：{}", e.getMessage());
            return Result.error("-1", "申请退款失败：" + e.getMessage());
        }
    }

    public Result<?> deleteBatch(List<Long> ids) {
        try {
            // 检查每个订单是否存在关联记录
            for (Long id : ids) {
                // 检查物流

            }

            int result = orderMapper.deleteBatchIds(ids);
            if (result > 0) {
                LOGGER.info("批量删除订单成功，删除数量：{}", result);
                return Result.success();
            }
            return Result.error("-1", "批量删除订单失败");
        } catch (Exception e) {
            LOGGER.error("批量删除订单失败：{}", e.getMessage());
            return Result.error("-1", "批量删除订单失败：" + e.getMessage());
        }
    }
    @Transactional
    public Result<?> payOrder(Long id){

            Order order = orderMapper.selectById(id);
            if (order == null) {
                return Result.error("-1", "未找到订单");
            }else{

                Product product=productMapper.selectById(order.getProductId());
                if (product!=null){
                    if(product.getStock()<order.getQuantity()){
                        return Result.error("-1", "库存不足");
                    }
                    product.setSalesCount(product.getSalesCount()+order.getQuantity());
                    product.setStock(product.getStock()-order.getQuantity());
                    order.setStatus(1);
                    int res = productMapper.updateById(product);

                    if(res<=0){
                        return  Result.error("-1","支付异常");
                    }
                    updateOrder(order.getId(),order);
                }

            }
        return Result.success();
    }

    public Result<?> updateOrderAddress(Long id, String address, String phone) {
        try {
            Order order = orderMapper.selectById(id);
            if (order == null) {
                return Result.error("-1", "未找到订单");
            }

            // 检查订单状态，只有未发货的订单才能修改地址
            if (order.getStatus() > 1) {
                return Result.error("-1", "订单已发货，无法修改收货地址");
            }

            order.setRecvAddress(address);
            order.setRecvPhone(phone);
            
            int result = orderMapper.updateById(order);
            if (result > 0) {
                LOGGER.info("更新订单收货信息成功，订单ID：{}", id);
                return Result.success(order);
            }
            return Result.error("-1", "更新订单收货信息失败");
        } catch (Exception e) {
            LOGGER.error("更新订单收货信息失败：{}", e.getMessage());
            return Result.error("-1", "更新订单收货信息失败：" + e.getMessage());
        }
    }
    public Result<?> updateOrder(Long id, Order order) {
        try {
            Order existingOrder = orderMapper.selectById(id);
            if (existingOrder == null) {
                return Result.error("-1", "未找到订单");
            }


            // 设置ID确保更新正确的订单
            order.setId(id);
            
            // 保持原有的不可修改字段
            order.setCreatedAt(existingOrder.getCreatedAt());
            order.setUserId(existingOrder.getUserId());
            order.setProductId(existingOrder.getProductId());
            order.setTotalPrice(existingOrder.getTotalPrice());
            
            int result = orderMapper.updateById(order);
            if (result > 0) {
                LOGGER.info("更新订单成功，订单ID：{}", id);
                return Result.success(order);
            }
            return Result.error("-1", "更新订单信息失败");
        } catch (Exception e) {
            LOGGER.error("更新订单失败：{}", e.getMessage());
            return Result.error("-1", "更新订单失败：" + e.getMessage());
        }
    }
} 