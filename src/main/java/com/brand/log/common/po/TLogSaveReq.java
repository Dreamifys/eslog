package com.brand.log.common.po;

public class TLogSaveReq {

    //日志请求标记
    private String tag;
    //日志值
    private String value;
    //日志业务key
    private String key;
    //日志请求唯一标识
    private String request_id;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
