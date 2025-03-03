package org.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Schema(description = "商品实体")
public class Product {
    @TableId(type = IdType.AUTO)
    @Schema(description = "商品ID")
    private Long id;

    @NotBlank(message = "商品名称不能为空")
    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品描述")
    private String description;

    @NotNull(message = "价格不能为空")
    @PositiveOrZero(message = "价格不能为负数")
    @Schema(description = "商品价格")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @PositiveOrZero(message = "库存不能为负数")
    @Schema(description = "库存数量")
    private Integer stock;

    @Schema(description = "是否开启折扣")
    private Integer isDiscount;
    @Schema(description = "折扣价格")
    private BigDecimal discountPrice;

    @NotNull(message = "分类ID不能为空")
    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "商品图片URL")
    private String imageUrl;

    @Schema(description = "销量")
    private Integer salesCount = 0;

    @NotNull(message = "农户ID不能为空")
    @Schema(description = "农户ID")
    private Long farmerId;

    @Schema(description = "商品状态：0-下架，1-上架")
    private Integer status = 1;

    @Schema(description = "创建时间")
    private Timestamp createdAt;

    @Schema(description = "更新时间")
    private Timestamp updatedAt;

    @TableField(exist = false)
    @Schema(description = "分类信息")
    private Category category;

    @TableField(exist = false)
    @Schema(description = "农户信息")
    private User farmer;
} 