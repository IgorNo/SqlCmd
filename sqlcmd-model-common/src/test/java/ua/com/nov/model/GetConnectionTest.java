package ua.com.nov.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import ua.com.nov.model.entity.DataBase;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class GetConnectionTest {

    @Test
    public void testGetMySqlLocalConnection() throws SQLException{
        DataSource dataSource = new MySqlLocalDataSource(new DataBase("sys", "root", "root"));
        Connection connection = dataSource.getConnection();
        assertTrue(connection != null);
    }

    @Test
    public void testGetPostgreSqlLocalConnection() throws SQLException{
        DataSource dataSource = new PostgreSqlLocalDataSource(new DataBase("postgres", "postgres", "postgres"));
        Connection connection = dataSource.getConnection();
        assertTrue(connection != null);
    }

    @Test
    public void testGetHyperSqlLocalConnection() throws SQLException{
        DataSource dataSource = new HyperSqlDataSource(new DataBase("sys", "root", "root"));
        Connection connection = dataSource.getConnection();
        assertTrue(connection != null);
    }
}
