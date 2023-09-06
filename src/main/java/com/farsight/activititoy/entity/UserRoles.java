package com.farsight.activititoy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户角色对应表
 * </p>
 *
 * @author farsight
 * @since 2023-09-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user_roles")
public class UserRoles extends Model<UserRoles> {

    private static final long serialVersionUID = 1L;
//    @TableId(type = IdType.INPUT)
//    private UserRolesKey key;
    private String userId;
    private String roleId;


    @Override
    public Serializable pkVal() {
        return this.roleId;
    }

}
