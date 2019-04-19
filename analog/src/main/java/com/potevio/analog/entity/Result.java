package com.potevio.analog.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "响应结果最外层类")
public class Result<T> {
    @ApiModelProperty(value = "成功失败标志，true为成功，false为失败", notes = "成功失败标志，true为成功，false为失败", required = true)
    private boolean flag;
    @ApiModelProperty(value = "返回码，20000为成功，20001为失败", notes = "返回码，20000为成功，20001为失败", required = true)
    private Integer code;
    @ApiModelProperty(value = "成功或失败信息信息", notes = "成功或失败信息信息", required = true)
    private String message;
    @ApiModelProperty(value = "返回的具体数据可能为null,String,POJO,列表", notes = "返回的具体数据可能为null,String,POJO,列表", required = false)
    private T data;

    public Result() {
    }

    public Result(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public Result(boolean flag, Integer code, String message, T data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
