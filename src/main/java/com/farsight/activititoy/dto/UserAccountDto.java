package com.farsight.activititoy.dto;

import com.farsight.activititoy.entity.UserAccount;
import lombok.Data;

@Data
public class UserAccountDto extends UserAccount {
    String repeatPassword;
}
