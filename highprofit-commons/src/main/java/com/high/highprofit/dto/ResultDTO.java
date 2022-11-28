package com.high.highprofit.dto;

/**
 * 响应DTO
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
public class ResultDTO {
    private String code;

    private String message;

    private Object result;

    public ResultDTO() {
    }

    public ResultDTO(String code, String message, Object result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + result +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
