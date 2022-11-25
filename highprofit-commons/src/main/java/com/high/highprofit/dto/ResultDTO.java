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

    private Object data;

    public ResultDTO() {
    }

    public ResultDTO(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
