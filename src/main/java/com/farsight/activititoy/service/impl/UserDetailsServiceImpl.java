package com.farsight.activititoy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.farsight.activititoy.entity.Permissions;
import com.farsight.activititoy.entity.RolePermissions;
import com.farsight.activititoy.entity.UserAccount;
import com.farsight.activititoy.entity.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实现springSecurity接口的类，是为了进行权限认证
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRolesServiceImp userRolesService;
    @Autowired
    private RolePermissionsServiceImp rolePermissionsService;
    @Autowired
    private PermissionsServiceImp permissionsService;
    @Autowired
    private UserAccountServiceImp userAccountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount databaseUser = userAccountService.getOne(new LambdaQueryWrapper<UserAccount>().eq(UserAccount::getUsername, username));
        databaseUser.setAuthorityList(getAuthorities(databaseUser));
        return databaseUser;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(UserAccount userAccount) {
        // 得到role的id
        List<String> roleIdList = userRolesService.list(
                        new LambdaQueryWrapper<UserRoles>().eq(UserRoles::getUserId, userAccount.getUid()))
                .stream().map(UserRoles::getRoleId).collect(Collectors.toList());
        List<String> permissionIdList = rolePermissionsService.list(
                        new LambdaQueryWrapper<RolePermissions>().in(RolePermissions::getRole, roleIdList))
                .stream().map(RolePermissions::getPermission).collect(Collectors.toList());
        List<String> permissionList = permissionsService.list(new LambdaQueryWrapper<Permissions>().in(Permissions::getId, permissionIdList))
                .stream().map(Permissions::getName).collect(Collectors.toList());
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 将权限信息转换为 GrantedAuthority 对象
        for (String permission : permissionList) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }

}
