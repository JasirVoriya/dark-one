package cn.darkone.framework.common.bytehi.utils;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class DataSourceUtils {
    public static Connection getConnection(DataSource dataSource) throws SQLException, IllegalStateException {
        return dataSource.getConnection();
    }
}
