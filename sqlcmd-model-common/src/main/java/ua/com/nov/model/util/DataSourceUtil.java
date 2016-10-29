package ua.com.nov.model.util;

import ua.com.nov.model.entity.Database;

import javax.sql.DataSource;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceUtil {
    public static final String HYPER_SQL_FILE_URL = "jdbc:hsqldb:file:lightdb/";

    public static final String HYPER_SQL_MEMORY_URL = "jdbc:hsqldb:mem:lightdb/";

    public static final String MY_SQL_LOCAL_URL = "jdbc:mysql://localhost:3306/";

    public static final String POSTGRE_SQL_LOCAL_URL = "jdbc:postgresql://localhost:5432/";

    public static Database getDatabase(Connection conn) throws SQLException {
        String url = conn.getMetaData().getURL();
        return new Database(getDatabaseName(url));
    }

    public static String getDatabaseName(String url) {
        int beginIndex = url.lastIndexOf('/') + 1;
        if (beginIndex < 0) throw new IllegalArgumentException(url);
        return url.substring(beginIndex);
    }

    public static String getDatabaseUrl(Connection conn) throws SQLException {
        String url = conn.getMetaData().getURL();
        return getDatabaseUrl(url);
    }

    public static String getDatabaseUrl(String url) {
        int endIndex = url.lastIndexOf('/') + 1;
        if (endIndex < 0) throw new IllegalArgumentException(url);
        return url.substring(0, endIndex);
    }
}
