package com.farsight.activititoy.exception;

import com.farsight.activititoy.uitl.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
@Slf4j
@Order(10000)
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result handleMyCustomException(BusinessException ex) {
        log.info("全局异常处理生效" + ex.getMessage());
        return Result.error(ex.getCodeMsg(), ex.getMessage());
    }
}
