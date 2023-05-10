package cn.darkone.framework.common.bytehi;


import cn.darkone.framework.common.bytehi.mapper.RowMapper;

import java.util.List;
import java.util.Map;

public interface CuteJdbcOperations {

    List<Map<String, Object>> execute(String sql, Object... args);

    /**
     * 查询一条数据
     * @param sql SQL语句
     * @param args 查询参数
     * @param rowMapper 行转换器
     * @param <T> 返回类型
     * @return 实体类对象
     */
    <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper);


    /**
     * 查询一条数据
     * @param sql SQL语句
     * @param rowMapper 行映射器
     * @param <T> 返回类型
     * @return 实体类对象
     */
    <T> T queryForObject(String sql, RowMapper<T> rowMapper);

    /**
     *  查询一条数据
     * @param sql SQL语句
     * @param requiredType 需要返回的类型
     * @param args 查询参数
     * @param <T> 返回类型
     * @return 实体类对象
     */
    <T> T queryForObject(String sql, Class<T> requiredType, Object... args);


    /**
     * 查询一条数据
     * @param sql SQL语句
     * @param requiredType 需要返回的类型
     * @param <T> 返回类型
     * @return 实体类对象
     */
    <T> T queryForObject(String sql, Class<T> requiredType);

    /**
     * 查询多条数据
     * @param sql SQL语句
     * @param rowMapper 行映射器
     * @param <T> 返回类型
     * @return 实体类List
     */
    <T> List<T> queryForList(String sql, RowMapper<T> rowMapper);
    /**
     * 查询多条数据
     * @param sql SQL语句
     * @param args 查询参数
     * @param rowMapper 行映射器
     * @param <T> 返回类型
     * @return 实体类List
     */
    <T> List<T> queryForList(String sql, Object[] args, RowMapper<T> rowMapper);

    /**
     * 查询多条数据
     * @param sql SQL语句
     * @param elementType 需要返回的类型
     * @return 实体类List
     */
    <T> List<T> queryForList(String sql, Class<T> elementType);

    /**
     * 查询多条数据
     * @param sql SQL语句
     * @param elementType 需要返回的类型
     * @param args 查询参数
     * @return 实体类List
     */
    <T> List<T> queryForList(String sql, Class<T> elementType, Object... args);


    /**
     * 批量修改
     * @param sql SQL更新语句
     * @param batchArgs 批量修改参数
     * @return 成功次数
     */
    int[] batchUpdate(String sql, List<Object[]> batchArgs);
    /**
     * 修改
     * @param sql SQL更新语句
     * @param args 修改参数
     * @return 成功次数
     */
    int update(String sql, Object... args);

}
