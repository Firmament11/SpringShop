package org.example.springboot.entity.Dict;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description ="字典表项实体类")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("dict_item")
public class DictItem {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
 @Schema(description ="字典类型code")
    private String dictTypeCode; // 关联sys_dict表的id字段
    @Schema(description ="字典表项键")
    private String itemKey;
    @Schema(description ="字典表项值")
    private String itemValue;
    @Schema(description ="字典描述")
    private String description;
    @Schema(description ="字典表项创建时间")
    @JsonFormat(locale="zh",timezone="GMT+8", pattern="yyyy-MM-dd")
    private LocalDateTime createTime;
    @Schema(description ="字典表项时间")
    @JsonFormat(locale="zh",timezone="GMT+8", pattern="yyyy-MM-dd")

    private LocalDateTime updateTime;
}
