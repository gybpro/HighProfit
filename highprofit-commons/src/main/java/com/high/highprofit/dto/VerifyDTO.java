package com.high.highprofit.dto;

import java.util.Map;

/**
 * 实名认证DTO
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
public class VerifyDTO {
    private Boolean charge;
    private String code;
    private String msg;
    private Integer remain;
    private String requestId;
    private Map<String, Object> result;

    public VerifyDTO() {
    }

    public VerifyDTO(Boolean charge, String code, String msg, Integer remain, String requestId, Map<String, Object> result) {
        this.charge = charge;
        this.code = code;
        this.msg = msg;
        this.remain = remain;
        this.requestId = requestId;
        this.result = result;
    }

    @Override
    public String toString() {
        return "VerifyDTO{" +
                "charge=" + charge +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", remain=" + remain +
                ", requestId='" + requestId + '\'' +
                ", result=" + result +
                '}';
    }

    public Boolean getCharge() {
        return charge;
    }

    public void setCharge(Boolean charge) {
        this.charge = charge;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}
