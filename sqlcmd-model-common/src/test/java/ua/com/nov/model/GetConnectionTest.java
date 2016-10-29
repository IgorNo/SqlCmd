package ua.com.nov.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import ua.com.nov.model.entity.Database;
import ua.com.nov.model.util.DataSourceUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class GetConnectionTest {

    @Test
    public void testGetMySqlLocalConnection() throws SQLException{
        String dbUrl = DataSourceUtil.MY_SQL_LOCAL_URL + "sys";
        DataSource dataSource = new SingleConnectionDataSource(new Database(dbUrl,"root", "root"));
        Connection connection = dataSource.getConnection();
        assertTrue(connection != null);
    }

    @Test
    public void testGetPostgreSqlLocalConnection() throws SQLException{
        String dbUrl = DataSourceUtil.POSTGRE_SQL_LOCAL_URL + "postgres";
        DataSource dataSource = new SingleThreadConnectionDataSource(new Database(dbUrl, "postgres", "postgres"));
        Connection connection = dataSource.getConnection();
        assertTrue(connection != null);
    }

    @Test
    public void testGetHyperSqlFileConnection() throws SQLException{
        String dbUrl = DataSourceUtil.HYPER_SQL_FILE_URL + "sys";
        DataSource dataSource = new MultiConnectionDataSource(new Database(dbUrl, "root", "root"));
        Connection connection = dataSource.getConnection();
        assertTrue(connection != null);
    }

    @Test
    public void testGetHyperSqlMemoryConnection() throws SQLException{
        String dbUrl = DataSourceUtil.HYPER_SQL_MEMORY_URL + "sys";
        DataSource dataSource = new MultiConnectionDataSource(new Database(dbUrl, "root", "root"));
        Connection connection = dataSource.getConnection();
        assertTrue(connection != null);
    }

}
