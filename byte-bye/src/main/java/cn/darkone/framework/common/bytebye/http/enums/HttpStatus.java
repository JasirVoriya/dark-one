package cn.darkone.framework.common.bytebye.http.enums;

public enum HttpStatus {
    OK(200, "OK"),
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    NOT_MODIFIED(304, "Not Modified"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported");
    private final int code;
    private final String status;

    HttpStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public int code() {
        return code;
    }

    public String status() {
        return status;
    }
}
