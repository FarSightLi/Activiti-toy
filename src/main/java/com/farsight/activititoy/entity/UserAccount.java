package com.farsight.activititoy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

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
public class UserAccount extends Model<UserAccount> implements UserDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "uid", type = IdType.ASSIGN_UUID)
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
     * 用户密码
     */
    @TableField("salt")
    private String salt;

    @TableField(exist = false)
    private Collection<? extends GrantedAuthority> authorityList;


    @Override
    public Serializable pkVal() {
        return this.uid;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
