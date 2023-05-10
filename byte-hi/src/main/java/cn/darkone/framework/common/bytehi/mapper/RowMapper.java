package cn.darkone.framework.common.bytehi.mapper;


import java.util.List;
import java.util.Map;

public interface RowMapper<T> {
    T mapRow(List<Map<String, Object>> results, int rowNum);
}