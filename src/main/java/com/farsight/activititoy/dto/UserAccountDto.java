package com.farsight.activititoy.dto;

import com.farsight.activititoy.entity.UserAccountEntity;
import lombok.Data;

@Data
public class UserAccountDto extends UserAccountEntity {
    String repeatPassword;
}
