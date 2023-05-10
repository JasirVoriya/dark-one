package cn.darkone.framework.common.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean hasLength(String str) {
        return (str != null && !str.isEmpty());
    }

    public static String firstToUpperCase(String old) {    // 将单词的首字母大写
        return old.substring(0, 1).toUpperCase() + old.substring(1);
    }

    /**
     * 驼峰转下划线,最后转为大写
     *
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        final Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString().toUpperCase();
    }

    /**
     * 下划线转驼峰,正常输出
     *
     * @param str
     * @return
     */
    public static String lineToHump(String str) {
        final Pattern linePattern = Pattern.compile("_(\\w)");
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
