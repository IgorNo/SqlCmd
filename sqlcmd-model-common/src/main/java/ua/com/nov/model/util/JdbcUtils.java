package ua.com.nov.model.util;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcUtils {

    public static void closeQuietly(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {/*NOP*/}

        }
    }

    public static void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (Exception e) {/*NOP*/}
        }
    }

    public static void closeQuietly(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            closeQuietly(resource);
        }
    }

    public static String getDatabaseProductName(String url) {
        int beginIndex = url.indexOf(':') + 1;
        if (beginIndex < 0) throw new IllegalArgumentException(url);
        int endIndex = url.indexOf(':', beginIndex);
        if (endIndex < 0) throw new IllegalArgumentException(url);
        return url.substring(beginIndex, endIndex);
    }

    public static String getDatabaseName(Connection conn) throws SQLException {
        String url = conn.getMetaData().getURL();
        return getDatabaseName(url);
    }

    public static String getDatabaseName(String fullURL) {
        int beginIndex = fullURL.lastIndexOf('/') + 1;
        if (beginIndex < 0) throw new IllegalArgumentException(fullURL);
        return fullURL.substring(beginIndex);
    }

    public static String getDatabaseUrl(Connection conn) throws SQLException {
        String url = conn.getMetaData().getURL();
        return getDatabaseUrl(url);
    }

    public static String getDatabaseUrl(String fullURL) {
        int endIndex = fullURL.lastIndexOf('/') + 1;
        if (endIndex < 0) throw new IllegalArgumentException(fullURL);
        return fullURL.substring(0, endIndex);
    }
}
