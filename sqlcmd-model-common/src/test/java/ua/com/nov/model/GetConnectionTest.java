package ua.com.nov.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import ua.com.nov.model.entity.Database;
import ua.com.nov.model.util.DataSourceUrl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class GetConnectionTest {

    @Test
    public void testGetMySqlLocalConnection() throws SQLException{
        DataSource dataSource = new SingleConnectionDataSource(DataSourceUrl.MY_SQL_LOCAL, new Database("sys", "root", "root"));
        Connection connection = dataSource.getConnection();
        assertTrue(connection != null);
    }

    @Test
    public void testGetPostgreSqlLocalConnection() throws SQLException{
        DataSource dataSource = new SingleThreadConnectionDataSource(DataSourceUrl.POSTGRE_SQL_LOCAL, new Database("postgres", "postgres", "postgres"));
        Connection connection = dataSource.getConnection();
        assertTrue(connection != null);
    }

    @Test
    public void testGetHyperSqlLocalConnection() throws SQLException{
        DataSource dataSource = new MultiConnectionDataSource(DataSourceUrl.HYPER_SQL, new Database("sys", "root", "root"));
        Connection connection = dataSource.getConnection();
        assertTrue(connection != null);
    }
}
