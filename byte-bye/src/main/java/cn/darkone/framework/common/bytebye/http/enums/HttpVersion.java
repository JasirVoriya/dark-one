package cn.darkone.framework.common.bytebye.http.enums;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1");
    private final String version;

    HttpVersion(String version) {
        this.version = version;
    }

    public String version() {
        return version;
    }
}
