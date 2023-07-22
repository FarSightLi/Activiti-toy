package com.farsight.activititoy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author farsight
 * @since 2023-07-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("toy_business")
public class ToyBusinessEntity extends Model<ToyBusinessEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 请假原因
     */
    @TableField("reason")
    private String reason;

    /**
     * 请假天数
     */
    @TableField("days")
    private Integer days;

    /**
     * 请假详情说明
     */
    @TableField("detail")
    private String detail;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
