package com.farsight.activititoy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 角色权限表
 * </p>
 *
 * @author farsight
 * @since 2023-09-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("role_permissions")
public class RolePermissions extends Model<RolePermissions> {

    private static final long serialVersionUID = 1L;

//    @TableId(type = IdType.INPUT)
//    private RolePermissionsKey key;

    private String role;

    private String permission;


    @Override
    public Serializable pkVal() {
        return this.permission;
    }

}
