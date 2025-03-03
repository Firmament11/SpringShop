package org.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Schema(description ="菜单类实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_menu")
public class Menu {
    @Schema(description ="菜单id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @Schema(description ="菜单名")
    private String name;

    @Schema(description ="菜单路径")
    private String path;

    @Schema(description ="菜单icon")
    private String icon;

    private String description;

    @Schema(description ="父级菜单id")
    private Integer pid;
    @Schema(description ="页面路径")
    private String pagePath;
    @Schema(description ="排序")
    private Integer sortNum;
    @TableField(exist = false)
    private List<Menu> children;
    @Schema(description ="菜单所属角色")
    private String role;
    @TableField(exist = false)
    @Builder.Default
    private Boolean hasChildren=false;
}
