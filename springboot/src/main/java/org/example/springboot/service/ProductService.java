package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.springboot.common.Result;
import org.example.springboot.entity.*;
import org.example.springboot.mapper.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private CarouselItemMapper carouselItemMapper;

    public Result<?> createProduct(Product product) {
        try {
            int result = productMapper.insert(product);
            if (result > 0) {
                LOGGER.info("创建商品成功，商品ID：{}", product.getId());
                return Result.success(product);
            }
            return Result.error("-1", "创建商品失败");
        } catch (Exception e) {
            LOGGER.error("创建商品失败：{}", e.getMessage());
            return Result.error("-1", "创建商品失败：" + e.getMessage());
        }
    }

    public Result<?> updateProduct(Long id, Product product) {
        Product oldProduct = productMapper.selectById(id);
        if(oldProduct!=null){
            String oldImg=oldProduct.getImageUrl();
            String newImg = product.getImageUrl();
            if(!oldImg.equals(newImg)){
                fileService.fileRemove(oldImg);
            }
        }
        product.setId(id);
        try {
            // 检查库存是否合法
            if (product.getStock() < 0) {
                return Result.error("-1", "库存不能为负数");
            }

            int result = productMapper.updateById(product);
            if (result > 0) {
                LOGGER.info("更新商品成功，商品ID：{}", id);
                return Result.success(product);
            }
            return Result.error("-1", "更新商品失败");
        } catch (Exception e) {
            LOGGER.error("更新商品失败：{}", e.getMessage());
            return Result.error("-1", "更新商品失败：" + e.getMessage());
        }
    }

    public Result<?> deleteProduct(Long id) {
        try {
            // 检查是否存在关联轮播图
            LambdaQueryWrapper<CarouselItem> carouselQuery = new LambdaQueryWrapper<>();
            carouselQuery.eq(CarouselItem::getProductId, id);
            Long carouselCount = carouselItemMapper.selectCount(carouselQuery);
            if (carouselCount > 0) {
                return Result.error("-1", "无法删除商品，存在关联轮播图记录");
            }

            // 检查是否存在关联订单
            LambdaQueryWrapper<Order> orderQuery = new LambdaQueryWrapper<>();
            orderQuery.eq(Order::getProductId, id);
            Long orderCount = orderMapper.selectCount(orderQuery);
            if (orderCount > 0) {
                return Result.error("-1", "无法删除商品，存在关联订单记录");
            }

            // 检查是否存在购物车记录
            LambdaQueryWrapper<Cart> cartQuery = new LambdaQueryWrapper<>();
            cartQuery.eq(Cart::getProductId, id);
            Long cartCount = cartMapper.selectCount(cartQuery);
            if (cartCount > 0) {
                return Result.error("-1", "无法删除商品，存在购物车记录");
            }

            // 检查是否存在评价记录
            LambdaQueryWrapper<Review> reviewQuery = new LambdaQueryWrapper<>();
            reviewQuery.eq(Review::getProductId, id);
            Long reviewCount = reviewMapper.selectCount(reviewQuery);
            if (reviewCount > 0) {
                return Result.error("-1", "无法删除商品，存在评价记录");
            }

            // 检查是否存在收藏记录
            LambdaQueryWrapper<Favorite> favoriteQuery = new LambdaQueryWrapper<>();
            favoriteQuery.eq(Favorite::getProductId, id);
            Long favoriteCount = favoriteMapper.selectCount(favoriteQuery);
            if (favoriteCount > 0) {
                return Result.error("-1", "无法删除商品，存在收藏记录");
            }

            int result = productMapper.deleteById(id);
            if (result > 0) {
                LOGGER.info("删除商品成功，商品ID：{}", id);
                return Result.success();
            }
            return Result.error("-1", "删除商品失败");
        } catch (Exception e) {
            LOGGER.error("删除商品失败：{}", e.getMessage());
            return Result.error("-1", "删除商品失败：" + e.getMessage());
        }
    }

    public Result<?> getProductById(Long id) {
        Product product = productMapper.selectById(id);
        if (product != null) {
            // 填充关联信息
            product.setFarmer(userMapper.selectById(product.getFarmerId()));
            product.setCategory(categoryMapper.selectById(product.getCategoryId()));
            return Result.success(product);
        }
        return Result.error("-1", "未找到商品");
    }

    public Page<Product> getProductsByPage(String name, Long categoryId, Long farmerId, Integer status,
                                      Integer currentPage, Integer size, String sortField, String sortOrder,
                                      Double minPrice, Double maxPrice) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        
        // 添加基本查询条件
        if (name != null) {
            queryWrapper.like("name", name);
        }
        if (categoryId != null) {
            queryWrapper.eq("category_id", categoryId);
        }
        if (farmerId != null) {
            queryWrapper.eq("farmer_id", farmerId);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }

        // 添加价格区间筛选
        if (minPrice != null || maxPrice != null) {
            queryWrapper.and(wrapper -> {
                wrapper.and(w -> {
                    w.eq("is_discount", 1);
                    if (minPrice != null) {
                        w.ge("discount_price", minPrice);
                    }
                    if (maxPrice != null) {
                        w.le("discount_price", maxPrice);
                    }
                }).or(w -> {
                    w.eq("is_discount", 0);
                    if (minPrice != null) {
                        w.ge("price", minPrice);
                    }
                    if (maxPrice != null) {
                        w.le("price", maxPrice);
                    }
                });
            });
        }
        
        // 修改价格排序逻辑
        if (sortField != null && !sortField.isEmpty()) {
            boolean isAsc = "asc".equalsIgnoreCase(sortOrder);
            switch (sortField) {
                case "sales":
                    queryWrapper.orderBy(true, isAsc, "sales_count");
                    break;
                case "price":
                    // 使用CASE WHEN语句进行排序
                    String orderSql = "CASE WHEN is_discount = 1 THEN discount_price ELSE price END";
                    queryWrapper.orderBy(true, isAsc, orderSql);
                    break;
                default:
                    queryWrapper.orderByDesc("created_at");
            }
        } else {
            queryWrapper.orderByDesc("created_at");
        }

        Page<Product> page = new Page<>(currentPage, size);
        Page<Product> result = productMapper.selectPage(page, queryWrapper);
        
        // 填充关联信息
        result.getRecords().forEach(product -> {
            product.setFarmer(userMapper.selectById(product.getFarmerId()));
            product.setCategory(categoryMapper.selectById(product.getCategoryId()));
        });
        
        return result;
    }

    public Result<?> updateProductStatus(Long id, Integer status) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            return Result.error("-1", "未找到商品");
        }
        product.setStatus(status);
        int result = productMapper.updateById(product);
        if (result > 0) {
            LOGGER.info("更新商品状态成功，商品ID：{}，新状态：{}", id, status);
            return Result.success();
        }
        return Result.error("-1", "更新商品状态失败");
    }

    public Result<?> deleteBatch(List<Long> ids) {
        try {
            // 检查每个商品是否存在关联记录
            for (Long id : ids) {
                // 检查轮播图
                LambdaQueryWrapper<CarouselItem> carouselQuery = new LambdaQueryWrapper<>();
                carouselQuery.eq(CarouselItem::getProductId, id);
                if (carouselItemMapper.selectCount(carouselQuery) > 0) {
                    return Result.error("-1", "无法删除商品ID：" + id + "，存在关联轮播图记录");
                }

                // 检查订单
                LambdaQueryWrapper<Order> orderQuery = new LambdaQueryWrapper<>();
                orderQuery.eq(Order::getProductId, id);
                if (orderMapper.selectCount(orderQuery) > 0) {
                    return Result.error("-1", "无法删除商品ID：" + id + "，存在关联订单记录");
                }

                // 检查购物车
                LambdaQueryWrapper<Cart> cartQuery = new LambdaQueryWrapper<>();
                cartQuery.eq(Cart::getProductId, id);
                if (cartMapper.selectCount(cartQuery) > 0) {
                    return Result.error("-1", "无法删除商品ID：" + id + "，存在购物车记录");
                }

                // 检查评价
                LambdaQueryWrapper<Review> reviewQuery = new LambdaQueryWrapper<>();
                reviewQuery.eq(Review::getProductId, id);
                if (reviewMapper.selectCount(reviewQuery) > 0) {
                    return Result.error("-1", "无法删除商品ID：" + id + "，存在评价记录");
                }

                // 检查收藏
                LambdaQueryWrapper<Favorite> favoriteQuery = new LambdaQueryWrapper<>();
                favoriteQuery.eq(Favorite::getProductId, id);
                if (favoriteMapper.selectCount(favoriteQuery) > 0) {
                    return Result.error("-1", "无法删除商品ID：" + id + "，存在收藏记录");
                }
            }

            int result = productMapper.deleteBatchIds(ids);
            if (result > 0) {
                LOGGER.info("批量删除商品成功，删除数量：{}", result);
                return Result.success();
            }
            return Result.error("-1", "批量删除商品失败");
        } catch (Exception e) {
            LOGGER.error("批量删除商品失败：{}", e.getMessage());
            return Result.error("-1", "批量删除商品失败：" + e.getMessage());
        }
    }
    @Transactional
    public Result<?> updateBatchStatus(List<Long> ids, Integer status) {
        try {
            // 检查状态值是否有效
            if (status != 0 && status != 1) {
                return Result.error("-1", "无效的商品状态值");
            }

            // 检查商品是否存在
            List<Product> products = productMapper.selectBatchIds(ids);
            if (products.isEmpty()) {
                return Result.error("-1", "未找到指定商品");
            }
            if (products.size() != ids.size()) {
                return Result.error("-1", "部分商品不存在");
            }

            // 逐个更新商品状态
            int successCount = 0;
            for (Product product : products) {
                product.setStatus(status);
                int result = productMapper.updateById(product);
                if (result > 0) {
                    successCount++;
                }
            }

            if (successCount == products.size()) {
                LOGGER.info("批量更新商品状态成功，更新数量：{}，新状态：{}", successCount, status);
                return Result.success();
            }
            return Result.error("-1", "部分商品状态更新失败");
        } catch (Exception e) {
            LOGGER.error("批量更新商品状态失败：{}", e.getMessage());
            return Result.error("-1", "批量更新商品状态失败：" + e.getMessage());
        }
    }
} 