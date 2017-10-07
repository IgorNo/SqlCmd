package ua.com.nov.model.entity.server;

import org.junit.Test;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.server.*;
import ua.com.nov.model.util.DbConstants;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class CreateServerTest {

    @Test
    public void testCreateMysqlSerer() throws SQLException {
        DataSource dataSource = new SingleConnectionDataSource(DbConstants.MY_SQL_LOCAL_URL, "",
                "root", "root");
        Connection conn = dataSource.getConnection();
        Server server = ServerFactory.createServer(conn);
        conn.close();
        assertTrue(server.getClass() == MysqlServer.class);
    }

    @Test
    public void testCreatePostgresqlSerer() throws SQLException {
        DataSource dataSource = new SingleConnectionDataSource(DbConstants.POSTGRE_SQL_LOCAL_URL, "",
                "postgres", "postgres");
        Connection conn = dataSource.getConnection();
        Server server = ServerFactory.createServer(conn);
        conn.close();
        assertTrue(server.getClass() == PostgresqlServer.class);
    }

    @Test
    public void testCreateHsqldbSerer() throws SQLException {
        DataSource dataSource = new SingleConnectionDataSource(DbConstants.HYPER_SQL_MEMORY_URL, "",
                null, null);
        Connection conn = dataSource.getConnection();
        Server server = ServerFactory.createServer(conn);
        conn.close();
        assertTrue(server.getClass() == HsqldbServer.class);
    }

}
