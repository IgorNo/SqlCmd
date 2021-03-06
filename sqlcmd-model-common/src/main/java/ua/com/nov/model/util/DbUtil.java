package ua.com.nov.model.util;

import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.database.HyperSqlDb;
import ua.com.nov.model.entity.metadata.database.MySqlDb;
import ua.com.nov.model.entity.metadata.database.PostgresSqlDb;

import java.sql.Connection;
import java.sql.SQLException;

public class DbUtil {
    public static final String HYPER_SQL_FILE_URL = "jdbc:hsqldb:file:lightdb/";

    public static final String HYPER_SQL_MEMORY_URL = "jdbc:hsqldb:mem:lightdb/";

    public static final String MY_SQL_LOCAL_URL = "jdbc:mysql://localhost:3306/";

    public static final String POSTGRE_SQL_LOCAL_URL = "jdbc:postgresql://localhost:5432/";

    public static final Database HYPER_SQL_MEM_SYSTEM_DB = new HyperSqlDb(HYPER_SQL_MEMORY_URL, "");

    public static final Database HYPER_SQL_FILE_SYSTEM_DB = new HyperSqlDb(HYPER_SQL_FILE_URL, "");

    public static final Database MY_SQL_LOCAL_SYSTEM_DB = new MySqlDb(MY_SQL_LOCAL_URL, "");

    public static final Database POSTGRES_SQL_LOCAL_SYSTEM_DB = new PostgresSqlDb(POSTGRE_SQL_LOCAL_URL , "");

    public static String getDatabaseProductName(String url) {
        int beginIndex = url.indexOf(':') + 1;
        if (beginIndex < 0) throw new IllegalArgumentException(url);
        int endIndex = url.indexOf(':', beginIndex);
        if (endIndex < 0) throw new IllegalArgumentException(url);
        return url.substring(beginIndex, endIndex);
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
