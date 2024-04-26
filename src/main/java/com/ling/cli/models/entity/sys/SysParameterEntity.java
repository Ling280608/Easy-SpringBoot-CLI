package com.ling.cli.models.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author ling
 * @description: 系统参数
 */
@Data
@Accessors(chain = true)
@TableName("sys_parameter")
public class SysParameterEntity {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long parameterId;
    /**
     * 键
     */
    @NotBlank()
    private String parameterKey;
    /**
     * 值
     */
    @NotBlank()
    private String parameterValue;
    /**
     * 备注
     */
    private String remark;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT)

    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 是否删除
     */
    @TableLogic
    private int isDel;
}
