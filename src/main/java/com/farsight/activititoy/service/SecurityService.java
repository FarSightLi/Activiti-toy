package com.farsight.activititoy.service;

import com.farsight.activititoy.dto.UserAccountDto;
import com.farsight.activititoy.entity.UserAccount;

public interface SecurityService {
    boolean register(UserAccountDto userAccountDto);

    String login(UserAccount userAccount);
}
