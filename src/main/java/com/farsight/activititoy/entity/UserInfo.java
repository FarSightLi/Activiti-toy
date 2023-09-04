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
import java.time.LocalDateTime;

/**
 * <p>
 * 用户详细信息表
 * </p>
 *
 * @author farsight
 * @since 2023-09-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user_info")
public class UserInfo extends Model<UserInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 电子邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 其他信息
     */
    @TableField("detail")
    private String detail;

    /**
     * 性别
     */
    @TableField("sex")
    private String sex;

    /**
     * 名字
     */
    @TableField("name")
    private String name;

    /**
     * 入职时间
     */
    @TableField("hiredate")
    private LocalDateTime hiredate;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
