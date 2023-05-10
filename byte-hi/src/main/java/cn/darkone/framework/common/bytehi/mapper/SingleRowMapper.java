package cn.darkone.framework.common.bytehi.mapper;


import cn.darkone.framework.common.core.utils.CuteBeanUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class SingleRowMapper<T> implements RowMapper<T> {

    private final Class<T> requiredType;

    public SingleRowMapper(Class<T> requiredType) {
        this.requiredType = requiredType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T mapRow(List<Map<String, Object>> results, int rowNum) {
        T instance;
        Map<String, Object> row = results.get(rowNum);
        String[] keys = row.keySet().toArray(new String[0]);
//        如果一行只有一个数据，我们要判断requiredType是Java基本的9个数据类型还是封装类型
        if (keys.length == 1) {
            String key = keys[0];
            Object value = row.get(key);
//            做类型检查，看看是否能够转换成泛型类型，因为泛型类型转换不会做语法检查
            Field field = CuteBeanUtils.getDeclaredField(requiredType, key);
            if (field == null) {
//                无法从需要类型里拿到对应的字段，说明无法转换成封装类型，判断是否能转换成基本类型
                if (requiredType.isInstance(value)) {
                    instance = (T) value;
                } else {
                    throw new ClassCastException(value.getClass().getTypeName() + " cannot be cast to " + requiredType.getTypeName());
                }
            } else {
                instance = CuteBeanUtils.newInstance(requiredType);
                assert instance != null;
                CuteBeanUtils.setter(instance, key, value, field.getType());
            }
        } else {
            instance = CuteBeanUtils.newInstance(requiredType);
            assert instance != null;
            for (String key : keys) {
                Object value = row.get(key);
                if (value == null) continue;
                Field field = CuteBeanUtils.getDeclaredField(requiredType, key);
                if (field == null) continue;
                if (field.getType().isInstance(value)) {
                    CuteBeanUtils.setter(instance, key, value, field.getType());
                } else {
                    throw new ClassCastException(value.getClass().getTypeName() + " cannot be cast to " + field.getType().getTypeName());
                }
            }
        }
        return instance;
    }
}
