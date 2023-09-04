package com.farsight.activititoy.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RolePermissionsKey implements Serializable {
    private String role;
    private String permission;
}
