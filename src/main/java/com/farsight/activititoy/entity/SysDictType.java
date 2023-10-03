package com.farsight.activititoy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName sys_dict_type
 */
@TableName(value = "sys_dict_type")
@Data
public class SysDictType implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名字
     */
    @TableField(value = "name")
    private String name;

    /**
     * 类型（英文名）
     */
    @TableField(value = "type")
    private String type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}