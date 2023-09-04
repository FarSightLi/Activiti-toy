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
 * 用户的账号密码表
 * </p>
 *
 * @author farsight
 * @since 2023-09-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user_account")
public class UserAccount extends Model<UserAccount> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "uid", type = IdType.AUTO)
    private String uid;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 用户密码
     */
    @TableField("password")
    private String password;

    /**
     * 是否启用
     */
    @TableField("enabled")
    private Boolean enabled;

    /**
     * 用户密码
     */
    @TableField("salt")
    private String salt;


    @Override
    public Serializable pkVal() {
        return this.uid;
    }

}
