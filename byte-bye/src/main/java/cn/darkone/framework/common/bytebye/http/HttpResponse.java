package cn.darkone.framework.common.bytebye.http;

import cn.darkone.framework.common.bytebye.http.enums.HttpHeaderKeys;
import cn.darkone.framework.common.bytebye.http.enums.HttpHeaderValues;
import cn.darkone.framework.common.bytebye.http.enums.HttpStatus;
import cn.darkone.framework.common.bytebye.http.enums.HttpVersion;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class HttpResponse {
    private HttpVersion version;
    private HttpStatus status;
    private final Map<String, String> headers = new HashMap<>();
    private byte[] body = new byte[0];

    public void addHeader(HttpHeaderKeys headerKey, HttpHeaderValues headerValue) {
        String key = headerKey.key();
        String value = headerValue.value();
        headers.put(key, value);
    }

    public void addHeader(HttpHeaderKeys headerKey, String value) {
        String key = headerKey.key();
        headers.put(key, value);
    }
}
