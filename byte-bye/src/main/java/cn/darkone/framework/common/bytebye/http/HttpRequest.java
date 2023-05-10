package cn.darkone.framework.common.bytebye.http;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class HttpRequest {
    private byte[] bytes;
    private String msg;
    private String method;
    private String path;
    private String version;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String[]> parameters = new HashMap<>();
    private String body;

    public String getParameter(String parameterName) {
        if (parameters.containsKey(parameterName))
            return parameters.get(parameterName)[0];
        return null;
    }
}
