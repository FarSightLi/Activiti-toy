//package com.farsight.activititoy.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.farsight.activititoy.entity.RolePermissionsEntity;
//import com.farsight.activititoy.entity.UserAccountEntity;
//import com.farsight.activititoy.entity.UserRolesEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//    @Autowired
//    private UserAccountServiceImp userAccountService;
//    @Autowired
//    private UserRolesServiceImp userRolesService;
//    @Autowired
//    private RolePermissionsServiceImp rolePermissionsService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserAccountEntity userAccount = userAccountService.getOne(new QueryWrapper<UserAccountEntity>().eq("username", username));
//        if (userAccount == null) {
//            throw new UsernameNotFoundException("该用户名不存在: " + username);
//        }
//        // 返回由于验证的User实体
//        return new org.springframework.security.core.userdetails.User(
//                userAccount.getUsername(),
//                userAccount.getPassword(),
//                userAccount.getEnabled(),
//                // 因为没有考虑这几个字段,所有全部设置为true
//                // 是否是否没有过期
//                true,
//                // 凭证（密码）是否没有过期
//                true,
//                // 账户是否没有被锁定
//                true,
//                // 查询该用户的权限
//                getAuthorities(userAccount.getUid())
//        );
//    }
//
//    // Load authorities from AuthorityRepository
//    private List<GrantedAuthority> getAuthorities(String userId) {
//        //通过UserID找到对应的Role
//        Set<String> roles = userRolesService.list(new QueryWrapper<UserRolesEntity>().eq("user_id", userId))
//                .stream()
//                .map(UserRolesEntity::getRoleId)
//                .collect(Collectors.toSet());
//        //通过Role找到authorities
//        Set<String> authorities = rolePermissionsService.list(new QueryWrapper<RolePermissionsEntity>().eq("role_id", roles))
//                .stream()
//                .map(RolePermissionsEntity::getPermissionId)
//                .collect(Collectors.toSet());
//        //返回权限
//        return authorities.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//    }
//}
