package cn.darkone.framework.common.web.utils;

import cn.darkone.framework.common.bytebye.http.HttpResponse;
import cn.darkone.framework.common.bytebye.http.enums.HttpHeaderKeys;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StaticResourceUtil {
    /**
     * 响应静态资源
     *
     * @param filepath 静态资源绝对路径
     * @param resp     响应对象
     * @throws IOException 文件不存在
     */
    public static void send(String filepath, HttpServletResponse resp) throws IOException {
        if (filepath == null) {
            // 无法获取到路径:
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Path path = Paths.get(filepath);
        if (!path.toFile().isFile()) {
            // 文件不存在:
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 根据文件名猜测Content-Type:
        String mime = Files.probeContentType(path);
        if (mime == null) {
            mime = "application/octet-stream";
        }
        resp.setContentType(mime);
        // 读取文件并写入Response:
        OutputStream output = resp.getOutputStream();
        try (InputStream input = new BufferedInputStream(new FileInputStream(filepath))) {
            input.transferTo(output);
        }
        output.flush();
    }

    /**
     * 响应静态资源
     *
     * @param filepath 静态资源绝对路径
     * @param resp     响应对象
     * @throws IOException 文件不存在
     */
    public static void sendDark(URI filepath, HttpResponse resp) throws IOException {
        if (filepath == null) {
            // 无法获取到路径:
//            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Path path = Paths.get(filepath);
        if (!path.toFile().isFile()) {
            // 文件不存在:
//            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 根据文件名猜测Content-Type:
        String mime = Files.probeContentType(path);
        if (mime == null) {
            mime = "application/octet-stream";
        }
        resp.addHeader(HttpHeaderKeys.CONTENT_TYPE, mime);
        // 读取文件并写入Response:
        try (InputStream input = new BufferedInputStream(new FileInputStream(path.toFile()))) {

            byte[] bytes = input.readAllBytes();
            resp.setBody(bytes);

        }
    }
}
