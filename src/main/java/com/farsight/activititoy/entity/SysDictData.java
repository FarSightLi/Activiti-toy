package com.farsight.activititoy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 字典值
 *
 * @TableName sys_dict_data
 */
@TableName(value = "sys_dict_data")
@Data
public class SysDictData implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联type表的字段
     */
    @TableField(value = "type")
    private String type;

    /**
     * 相当于key
     */
    @TableField(value = "label")
    private String label;

    /**
     * 相当于value
     */
    @TableField(value = "value")
    private Integer value;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}