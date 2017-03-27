package ua.com.nov.model;

import org.junit.Test;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.datasource.SingleThreadConnectionDataSource;
import ua.com.nov.model.entity.metadata.database.HyperSqlDb;
import ua.com.nov.model.entity.metadata.database.MySqlDb;
import ua.com.nov.model.entity.metadata.database.PostgresSqlDb;
import ua.com.nov.model.util.DbUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class GetConnectionTest {

    @Test
    public void testGetMySqlLocalConnection() throws SQLException{
        DataSource dataSource = new SingleConnectionDataSource(new MySqlDb(DbUtil.MY_SQL_LOCAL_URL, "sys"),
                "root", "root");
        Connection conn = dataSource.getConnection();
        System.out.println(conn.getMetaData().getDatabaseProductName());
        assertTrue(conn != null);
        Connection conn1 = dataSource.getConnection();
        assertTrue(conn1 == conn);
    }

    @Test
    public void testGetPostgreSqlLocalConnection() throws SQLException{
        String dbUrl = DbUtil.POSTGRE_SQL_LOCAL_URL;
        DataSource dataSource = new SingleThreadConnectionDataSource(new PostgresSqlDb(dbUrl , "postgres"),
                "postgres", "postgres");
        Connection conn = dataSource.getConnection();
        System.out.println(conn.getMetaData().getDatabaseProductName());
        assertTrue(conn != null);
        Connection conn1 = dataSource.getConnection();
        assertTrue(conn1 == conn);
    }

    @Test
    public void testGetHyperSqlFileConnection() throws SQLException{
        String dbUrl = DbUtil.HYPER_SQL_FILE_URL;
        DataSource dataSource = new HyperSqlDb(dbUrl, "hsql");
        Connection conn = dataSource.getConnection(null, null);
        System.out.println(conn.getMetaData().getDatabaseProductName());
        assertTrue(conn != null);
        Connection conn1 = dataSource.getConnection(null, null);
        assertTrue(conn1 != conn);
    }

    @Test
    public void testGetHyperSqlMemoryConnection() throws SQLException{
        String dbUrl = DbUtil.HYPER_SQL_MEMORY_URL;
        DataSource dataSource = new HyperSqlDb(dbUrl, "hsql");
        Connection conn = dataSource.getConnection("anyName", "anyPassword");
        System.out.println(conn.getMetaData().getDatabaseProductName());
        assertTrue(conn != null);
        Connection conn1 = dataSource.getConnection("anyName", "anyPassword");
        assertTrue(conn1 != conn);
    }

}
