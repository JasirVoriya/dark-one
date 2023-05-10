package cn.darkone.framework.common.bytebye.http.enums;

public enum HttpHeaderKeys {
    CONTENT_TYPE("Content-Type"), CONNECTION("Connection");

    private final String key;

    HttpHeaderKeys(String key) {
        this.key=key;
    }

    public String key() {
        return key;
    }
}
