package cn.darkone.framework.common.web.dispatchers;

import cn.darkone.framework.common.bytebye.http.HttpRequest;
import cn.darkone.framework.common.bytebye.http.HttpResponse;
import cn.darkone.framework.common.core.utils.CuteBeanUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
public abstract class Dispatcher {
    private final Object instance; // Controller实例
    private final Method method; // Controller方法
    private final String[] parameterNames; // 方法参数名称
    private final Class<?>[] parameterClasses; // 方法参数类型
    private final ObjectMapper objectMapper;

    public Dispatcher(Object instance, Method method) {
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        this.instance = instance;
        this.method = method;
        this.parameterNames = discoverer.getParameterNames(method);
        this.parameterClasses = method.getParameterTypes();
        this.objectMapper = new ObjectMapper();

        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

        objectMapper.registerModule(javaTimeModule);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    @SneakyThrows
    public void invoke(HttpServletRequest request, HttpServletResponse response) {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/json");


        Object[] arguments = getArguments(request, response);
        Object result = this.method.invoke(this.instance, arguments);
        String string = objectMapper.writeValueAsString(result);
        response.getWriter().write(string);
        response.flushBuffer();
    }


    @SneakyThrows
    protected Object[] getArguments(HttpServletRequest request, HttpServletResponse response) {
        Object[] arguments = new Object[parameterClasses.length];

        for (int i = 0; i < parameterClasses.length; i++) {
            String parameterName = parameterNames[i];
            Class<?> parameterClass = parameterClasses[i];
            if (parameterClass == HttpServletRequest.class) {
                arguments[i] = request;
                continue;
            }
            if (parameterClass == HttpServletResponse.class) {
                arguments[i] = response;
                continue;
            }
            if (parameterClass == HttpSession.class) {
                arguments[i] = request.getSession();
                continue;
            }
//                数组
            if (parameterClass.isArray()) {
                arguments[i] = ConvertUtils.convert(request.getParameterMap().get(parameterName), parameterClass);
                continue;
            }
//                基本数据类型或者包装类型
            if (CuteBeanUtils.isCommonDataType(parameterClass) || CuteBeanUtils.isWrapClass(parameterClass)) {
                arguments[i] = ConvertUtils.convert(request.getParameter(parameterName), parameterClass);
                continue;
            }
//                字符串
            if (parameterClass == String.class) {
                arguments[i] = request.getParameter(parameterName);
                continue;
            }
//            枚举类
            if (parameterClass.isEnum()) {
                arguments[i] = MethodUtils.invokeStaticMethod(parameterClass, "valueOf", request.getParameter(parameterName));
                continue;
            }
//                不是基本类型，可能是一个JavaBean
            Object instance = CuteBeanUtils.newInstance(parameterClass);
            arguments[i] = instance;
            try {
                BeanUtils.copyProperties(instance, request.getParameterMap());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("Missing handler for type: " + parameterClass);
            }
        }
        return arguments;
    }

    @SneakyThrows
    public void invokeDark(HttpRequest request, HttpResponse response) {
        Object[] arguments = getArgumentsDark(request);
        Object result = this.method.invoke(this.instance, arguments);
        byte[] bytes = objectMapper.writeValueAsBytes(result);
        response.setBody(bytes);
    }

    @SneakyThrows
    protected Object[] getArgumentsDark(HttpRequest request) {
        Object[] arguments = new Object[parameterClasses.length];

        for (int i = 0; i < parameterClasses.length; i++) {
            String parameterName = parameterNames[i];
            Class<?> parameterClass = parameterClasses[i];
            if (parameterClass == HttpServletRequest.class) {
                arguments[i] = request;
                continue;
            }
//                数组
            if (parameterClass.isArray()) {
                arguments[i] = ConvertUtils.convert(request.getParameters().get(parameterName), parameterClass);
                continue;
            }
//                基本数据类型或者包装类型
            if (CuteBeanUtils.isCommonDataType(parameterClass) || CuteBeanUtils.isWrapClass(parameterClass)) {
                arguments[i] = ConvertUtils.convert(request.getParameter(parameterName), parameterClass);
                continue;
            }
//                字符串
            if (parameterClass == String.class) {
                arguments[i] = request.getParameter(parameterName);
                continue;
            }
//            枚举类
            if (parameterClass.isEnum()) {
                arguments[i] = MethodUtils.invokeStaticMethod(parameterClass, "valueOf", request.getParameter(parameterName));
                continue;
            }
//                不是基本类型，可能是一个JavaBean
            Object instance = CuteBeanUtils.newInstance(parameterClass);
            arguments[i] = instance;
            try {
                BeanUtils.copyProperties(instance, request.getParameters());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("Missing handler for type: " + parameterClass);
            }
        }
        return arguments;
    }
}
