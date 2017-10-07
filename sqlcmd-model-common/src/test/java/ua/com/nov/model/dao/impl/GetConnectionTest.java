package ua.com.nov.model.dao.impl;

import org.junit.Test;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.datasource.SingleThreadConnectionDataSource;
import ua.com.nov.model.util.DbConstants;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class GetConnectionTest {

    @Test
    public void testGetMySqlLocalConnection() throws SQLException {
        DataSource dataSource = new SingleConnectionDataSource(DbConstants.MY_SQL_LOCAL_URL, "",
                "root", "root");
        Connection conn = dataSource.getConnection();
        System.out.println(conn.getMetaData().getDatabaseProductName());
        assertTrue(conn != null);
        Connection conn1 = dataSource.getConnection();
        assertTrue(conn1 == conn);
        conn.close();
    }

    @Test
    public void testGetPostgreSqlLocalConnection() throws SQLException {
        DataSource dataSource = new SingleThreadConnectionDataSource(DbConstants.POSTGRE_SQL_LOCAL_URL, "",
                "postgres", "postgres");
        Connection conn = dataSource.getConnection();
        System.out.println(conn.getMetaData().getDatabaseProductName());
        assertTrue(conn != null);
        Connection conn1 = dataSource.getConnection();
        assertTrue(conn1 == conn);
        conn.close();
    }

    @Test
    public void testGetHyperSqlFileConnection() throws SQLException {
        DataSource dataSource = new SingleThreadConnectionDataSource(DbConstants.HYPER_SQL_FILE_URL, "",
                null, null);
        Connection conn = dataSource.getConnection();
        System.out.println(conn.getMetaData().getDatabaseProductName());
        assertTrue(conn != null);
        Connection conn1 = dataSource.getConnection();
        assertTrue(conn1 == conn);
    }

    @Test
    public void testGetHyperSqlMemoryConnection() throws SQLException{
        DataSource dataSource = new SingleThreadConnectionDataSource(DbConstants.HYPER_SQL_MEMORY_URL, "",
                null, null);
        Connection conn = dataSource.getConnection();
        System.out.println(conn.getMetaData().getDatabaseProductName());
        assertTrue(conn != null);
        Connection conn1 = dataSource.getConnection();
        assertTrue(conn1 == conn);
    }

}
