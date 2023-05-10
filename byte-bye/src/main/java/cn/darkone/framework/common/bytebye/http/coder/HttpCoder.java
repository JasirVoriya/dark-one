package cn.darkone.framework.common.bytebye.http.coder;

import cn.darkone.framework.common.bytebye.http.HttpRequest;
import cn.darkone.framework.common.bytebye.http.HttpResponse;
import cn.darkone.framework.common.core.utils.ArrayUtils;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HttpCoder {
    private static final String bodyBeginFlag = "\r\n\r\n";

    public static HttpRequest decoder(byte[] bytes) {
        String msg = new String(bytes, StandardCharsets.UTF_8);
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setMsg(msg);
        httpRequest.setBytes(bytes);
        try {
            String[] msgLine = msg.split("\r\n");
            decodeLine(httpRequest, msgLine);
            decodeHeaders(httpRequest, msgLine);
            decodeBody(httpRequest, msg);
            return httpRequest;
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] encoder(HttpResponse response) {
        String version = response.getVersion().version();
        int code = response.getStatus().code();
        String status = response.getStatus().status();
        StringBuilder headers = new StringBuilder();
        response.getHeaders().forEach((key, value) -> headers.append(key).append(": ").append(value).append("\r\n"));

        byte[] bytes = (version + " " + code + " " + status + "\r\n" + headers + "\r\n").getBytes(StandardCharsets.UTF_8);
        return ArrayUtils.byteConcat(bytes, response.getBody());
    }

    private static void decodeParameters(HttpRequest httpRequest, String parameters) {
        Map<String, List<String>> parameterMap = new LinkedHashMap<>();
        for (String parameter : parameters.split("&")) {
            String[] param = parameter.split("=");
            if (param.length == 2) {
                if (!parameterMap.containsKey(param[0])) {
                    parameterMap.put(param[0], new LinkedList<>());
                }
                parameterMap.get(param[0]).add(param[1]);
            }
        }
        parameterMap.forEach((key, list) -> httpRequest.getParameters().put(key, list.toArray(new String[0])));
    }

    private static void decodeLine(HttpRequest httpRequest, String[] msgLine) {

        String[] lines = msgLine[0].split(" ");
        String[] paths = lines[1].split("\\?");
        httpRequest.setMethod(lines[0]);
        httpRequest.setPath(paths[0]);
        if (paths.length == 2) decodeParameters(httpRequest, paths[1]);
        httpRequest.setVersion(lines[2]);
    }

    private static void decodeHeaders(HttpRequest httpRequest, String[] msgLine) {
        int lineIndex = 1;
        while (lineIndex < msgLine.length && !"".equals(msgLine[lineIndex])) {
            String[] split = msgLine[lineIndex].split(": ");
            httpRequest.getHeaders().put(split[0], split[1]);
            lineIndex++;
        }
    }

    private static void decodeBody(HttpRequest httpRequest, String msg) {
        String lenStr = httpRequest.getHeaders().get("Content-Length");
        if (lenStr == null) return;
        int bodySize = Integer.parseInt(lenStr);
        int begin = msg.indexOf(bodyBeginFlag) + bodyBeginFlag.length();
        String body = msg.substring(begin, begin + bodySize);
        httpRequest.setBody(body);
    }
}
