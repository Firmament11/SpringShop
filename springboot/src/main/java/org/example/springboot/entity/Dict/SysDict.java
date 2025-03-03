package org.example.springboot.entity.Dict;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Schema(description ="字典类型实体类")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dict")
public class SysDict {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @Schema(description ="字典类型编码")
    private String dictTypeCode;
    @Schema(description ="字典类型名称")
    private String dictTypeName;
    @Schema(description ="字典类型描述")
    private String description;
    @Schema(description ="字典类型排序")
    private Integer sortOrder;
    @Schema(description ="字典类型创建时间")
    private LocalDateTime createTime;
    @Schema(description ="字典类型更新时间")
    private LocalDateTime updateTime;
}
