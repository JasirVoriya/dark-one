package cn.darkone.framework.common.bytehi;



import cn.darkone.framework.common.bytehi.mapper.RowMapper;
import cn.darkone.framework.common.bytehi.mapper.SingleRowMapper;
import cn.darkone.framework.common.bytehi.utils.DataSourceUtils;
import cn.darkone.framework.common.core.utils.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CuteJdbcTemplate implements CuteJdbcOperations {
    private final DataSource dataSource;

    public CuteJdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Map<String, Object>> execute(String sql, Object... args) {
        try (Connection connection = DataSourceUtils.getConnection(this.dataSource)) {
            assert connection != null;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                if (args != null) {
                    setArgs(ps, args);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    List<Map<String, Object>> results = new LinkedList<>();
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    while (rs.next()) {
                        LinkedHashMap<String, Object> res = new LinkedHashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            String key = StringUtils.lineToHump(metaData.getColumnLabel(i));
                            Object value = rs.getObject(i);
                            res.put(key, value);
                        }
                        results.add(res);
                    }
                    return results;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) {
        List<Map<String, Object>> mapList = execute(sql, args);
        if (mapList.size() == 0) return null;
        return rowMapper.mapRow(mapList, 0);
    }

    @Override
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper) {
        List<Map<String, Object>> mapList = execute(sql);
        if (mapList.size() == 0) return null;
        return rowMapper.mapRow(mapList, 0);
    }

    @Override
    public <T> T queryForObject(String sql, Class<T> requiredType) {
        return queryForObject(sql, new SingleRowMapper<>(requiredType));
    }

    @Override
    public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) {
        return queryForObject(sql, args, new SingleRowMapper<>(requiredType));
    }

    @Override
    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper) {
        List<Map<String, Object>> mapList = execute(sql);
        List<T> res = new LinkedList<>();
        for (int i = 0; i < mapList.size(); i++) {
            res.add(rowMapper.mapRow(mapList, i));
        }
        return res;
    }

    @Override
    public <T> List<T> queryForList(String sql, Object[] args, RowMapper<T> rowMapper) {
        List<Map<String, Object>> mapList = execute(sql, args);
        List<T> res = new LinkedList<>();
        for (int i = 0; i < mapList.size(); i++) {
            res.add(rowMapper.mapRow(mapList, i));
        }
        return res;
    }


    @Override
    public <T> List<T> queryForList(String sql, Class<T> elementType) {
        return queryForList(sql, new SingleRowMapper<>(elementType));
    }

    @Override
    public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) {
        return queryForList(sql, args, new SingleRowMapper<>(elementType));
    }


    @Override
    public int update(String sql, Object... args) {
        List<Object[]> batchArgs = new LinkedList<>();

        batchArgs.add(args);
        return batchUpdate(sql, batchArgs)[0];
    }

    @Override
    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        try (Connection connection = DataSourceUtils.getConnection(this.dataSource)) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                for (Object[] args : batchArgs) {
                    setArgs(ps, args);
                    ps.addBatch();
                }
                return ps.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new int[]{0};
        }
    }

    private void setArgs(PreparedStatement ps, Object[] args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            ps.setObject(i + 1, args[i]);
        }
    }
}
