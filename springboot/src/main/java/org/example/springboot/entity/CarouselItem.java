package org.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "轮播图实体")
public class CarouselItem {
    @TableId(type = IdType.AUTO)
    @Schema(description = "轮播图ID")
    private Long id;

    @Schema(description = "轮播图片URL")
    private String imageUrl;

    @Schema(description = "标签文本")
    private String tag;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述文本")
    private String description;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "排序顺序")
    private Integer sortOrder;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;

    @TableField(exist = false)
    @Schema(description = "关联的商品信息")
    private Product product;
} 