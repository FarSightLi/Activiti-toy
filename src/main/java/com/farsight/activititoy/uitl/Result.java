package com.farsight.activititoy.uitl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    private Boolean isSuccess;
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success(String msg, T data) {
        return new Result<T>(true, 200, msg, data);
    }

    public static <T> Result<T> fail(Integer code, String msg, T data) {
        return new Result<T>(false, code, msg, data);
    }
}
