package com.farsight.activititoy.service;

import com.farsight.activititoy.dto.UserAccountDto;

public interface SecurityService {
    boolean register(UserAccountDto userAccountDto);
}
