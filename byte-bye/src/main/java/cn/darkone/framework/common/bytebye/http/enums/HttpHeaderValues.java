package cn.darkone.framework.common.bytebye.http.enums;

public enum HttpHeaderValues {
    TEXT_HTML("text/html"), CLOSE("close");

    private final String value;

    HttpHeaderValues(String value) {
        this.value=value;
    }

    public String value() {
        return value;
    }
}
