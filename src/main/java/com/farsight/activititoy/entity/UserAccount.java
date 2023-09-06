package com.farsight.activititoy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.farsight.activititoy.dao.PermissionsDao;
import com.farsight.activititoy.dao.RolePermissionsDao;
import com.farsight.activititoy.dao.RolesDao;
import com.farsight.activititoy.dao.UserRolesDao;
import com.farsight.activititoy.service.impl.UserDetailsServiceImpl;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
