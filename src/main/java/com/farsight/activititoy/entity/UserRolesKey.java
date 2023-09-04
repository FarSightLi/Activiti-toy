package com.farsight.activititoy.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRolesKey implements Serializable {

    private String userId;

    private String roleId;
}
