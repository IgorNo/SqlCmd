package ua.com.nov.model.dao.impl;

import org.junit.Test;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.datasource.SingleThreadConnectionDataSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.server.MySqlServer;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.util.DbUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class GetConnectionTest {

    @Test
    public void testGetMySqlLocalConnection() throws SQLException {
        Server server = new MySqlServer(DbUtils.MY_SQL_LOCAL_URL);
        DataSource dataSource = new SingleConnectionDataSource(new Database(server, ""),
                "root", "root");
        Connection conn = dataSource.getConnection();
        System.out.println(conn.getMetaData().getDatabaseProductName());
        assertTrue(conn != null);
        Connection conn1 = dataSource.getConnection();
        assertTrue(conn1 == conn);
    }

    @Test
    public void testGetPostgreSqlLocalConnection() throws SQLException {
        Server server = new MySqlServer(DbUtils.POSTGRE_SQL_LOCAL_URL);
        DataSource dataSource = new SingleThreadConnectionDataSource(new Database(server , ""),
                "postgres", "postgres");
        Connection conn = dataSource.getConnection();
        System.out.println(conn.getMetaData().getDatabaseProductName());
        assertTrue(conn != null);
        Connection conn1 = dataSource.getConnection();
        assertTrue(conn1 == conn);
    }

    @Test
    public void testGetHyperSqlFileConnection() throws SQLException {
        Server server = new MySqlServer(DbUtils.HYPER_SQL_FILE_URL);
        DataSource dataSource = new Database(server, "");
        Connection conn = dataSource.getConnection(null, null);
        System.out.println(conn.getMetaData().getDatabaseProductName());
        assertTrue(conn != null);
        Connection conn1 = dataSource.getConnection(null, null);
        assertTrue(conn1 != conn);
    }

    @Test
    public void testGetHyperSqlMemoryConnection() throws SQLException{
        Server server = new MySqlServer(DbUtils.HYPER_SQL_MEMORY_URL);
        DataSource dataSource = new Database(server, "hsql");
        Connection conn = dataSource.getConnection("anyName", "anyPassword");
        System.out.println(conn.getMetaData().getDatabaseProductName());
        assertTrue(conn != null);
        Connection conn1 = dataSource.getConnection("anyName", "anyPassword");
        assertTrue(conn1 != conn);
    }

}
