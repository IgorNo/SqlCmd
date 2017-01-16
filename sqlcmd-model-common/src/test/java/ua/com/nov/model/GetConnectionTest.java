package ua.com.nov.model;

import org.junit.Test;
import ua.com.nov.model.datasource.MultiConnectionDataSource;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.datasource.SingleThreadConnectionDataSource;
import ua.com.nov.model.entity.database.HyperSqlDb;
import ua.com.nov.model.entity.database.MySqlDb;
import ua.com.nov.model.entity.database.PostgreSqlDb;
import ua.com.nov.model.util.DataSourceUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class GetConnectionTest {

    @Test
    public void testGetMySqlLocalConnection() throws SQLException{
        String dbUrl = DataSourceUtil.MY_SQL_LOCAL_URL + "sys";
        DataSource dataSource = new SingleConnectionDataSource(new MySqlDb(dbUrl,"root", "root"));
        Connection connection = dataSource.getConnection();
        System.out.println(connection.getMetaData().getDatabaseProductName());
        assertTrue(connection != null);
    }

    @Test
    public void testGetPostgreSqlLocalConnection() throws SQLException{
        String dbUrl = DataSourceUtil.POSTGRE_SQL_LOCAL_URL + "postgres";
        DataSource dataSource = new SingleThreadConnectionDataSource(new PostgreSqlDb(dbUrl, "postgres", "postgres"));
        Connection connection = dataSource.getConnection();
        System.out.println(connection.getMetaData().getDatabaseProductName());
        assertTrue(connection != null);
    }

    @Test
    public void testGetHyperSqlFileConnection() throws SQLException{
        String dbUrl = DataSourceUtil.HYPER_SQL_FILE_URL + "sys";
        DataSource dataSource = new MultiConnectionDataSource(new HyperSqlDb(dbUrl, "root", "root"));
        Connection connection = dataSource.getConnection();
        System.out.println(connection.getMetaData().getDatabaseProductName());
        assertTrue(connection != null);
    }

    @Test
    public void testGetHyperSqlMemoryConnection() throws SQLException{
        String dbUrl = DataSourceUtil.HYPER_SQL_MEMORY_URL + "sys";
        DataSource dataSource = new MultiConnectionDataSource(new HyperSqlDb(dbUrl, "root", "root"));
        Connection connection = dataSource.getConnection();
        System.out.println(connection.getMetaData().getDatabaseProductName());
        assertTrue(connection != null);
    }

}
