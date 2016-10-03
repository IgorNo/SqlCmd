package ua.com.nov.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class GetConnectionTest {

    @Test
    public void testGetMySqlLocalConnection() throws SQLException{
        DataSource dataSource = new MySqlLocalDataSource("eshop", "root", "root");
        Connection connection = dataSource.getConnection();
        assertTrue(connection != null);
    }

    @Test
    public void testGetPostgreSqlLocalConnection() throws SQLException{
        DataSource dataSource = new PostgreSqlLocalDataSource("test", "postgres", "postgre");
        Connection connection = dataSource.getConnection();
        assertTrue(connection != null);
    }

    @Test
    public void testGetHyperSqlLocalConnection() throws SQLException{
        DataSource dataSource = new HyperSqlDataSource("test", "root", "root");
        Connection connection = dataSource.getConnection();
        assertTrue(connection != null);
    }


}
