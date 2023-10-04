package com.farsight.activititoy.exception;

public enum CodeMsg {
    //成功
    SUCCESS(0, "操作成功"),
    DICT_ERROR(1002, "字典错误"),
    JACKSON_ERROR(1003, "jackson转换错误，请查看日志"),
    TOKEN_ERROR(5000, "Token错误"),
    SYSTEM_ERROR(10001, "系统错误"),
    SERVER_RESOURCES_UNAVAILABLE(10002, "服务端资源不可用"),
    REMOTE_SERVICE_ERROR(10003, "远程服务出错"),
    PARAMETER_ERROR(10008, "参数错误，请参考API文档"),
    BAD_REQUEST(10012, "非法请求"),
    HTTPMETHD_NONSUPPORT(10021, "请求的HTTP METHOD不支持"),
    API_ABANDONED(10026, "该接口已经废弃"),
    APIRESULT_FOMATTERERROR(10027, "接口返回值格式异常"),
    API_UNAUTHORIZED(10028, "接口未授权"),
    API_CALLERROR(10029, "接口调用异常"),
    OTHER_ERROR(11000, "其它错误"),
    LICENSE_ERROR(12000, "License授权错误"),
    DB_NETWORK_EXCPTION(50001, "数据库连接异常"),
    DB_TIMEOUT(50002, "数据库响应超时"),
    DB_PRIMARKKEY_CONFLICT(50003, "数据库违反唯一约束"),
    DB_CONSTRAINT_ERROR(50004, "数据违反数据库检查约束"),
    DB_OTHER_ERROR(51000, "其他未知数据库相关问题");

    private int code;
    private String codeRemark;

    CodeMsg() {
    }

    CodeMsg(int code, String codeRemark) {
        this.code = code;
        this.codeRemark = codeRemark;
    }

    @Override
    public String toString() {
        return "CodeMsg [code=" + code + ", codeRemark=" + codeRemark + "]";
    }

    public int getCode() {
        return code;
    }

    public String getCodeRemark() {
        return codeRemark;
    }
}
