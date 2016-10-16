package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.HyperSqlDataSource;
import ua.com.nov.model.MySqlLocalDataSource;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.DataBase;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertTrue;

public class HyperSqlDataBaseDaoTest {
    private static final String DROP_DB_SQL = "DROP DATABASE IF EXISTS ";

    private static DataSource dataSource = new MySqlLocalDataSource(new DataBase("sys", "root", "root"));
    private static AbstractDao dao = new HyperSqlDataBaseDao();
    private static DataBase tmpDataBase = new DataBase("tmp", "root", "root");

    @Before
    public void setUp() throws SQLException {
        dao.setDataSource(dataSource);
        dao.create(tmpDataBase);
    }

    @Test
    public void testCreateDataBase() throws SQLException {
        DataSource tmpDataSource = new HyperSqlDataSource(tmpDataBase);
        Connection conn = tmpDataSource.getConnection();
        assertTrue(conn != null);
        conn.close();
    }

    @Test(expected = AssertionError.class)
    public void testDeleteDataBase() throws SQLException {
        dao.delete(tmpDataBase);
        DataSource tmpDataSource = new HyperSqlDataSource(tmpDataBase);
        Connection conn = tmpDataSource.getConnection();
        assertTrue(conn == null);
        conn.close();
    }

    @After
    public void clouseUp() throws SQLException{
        Statement statement = dataSource.getConnection().createStatement();
        statement.executeUpdate(DROP_DB_SQL + tmpDataBase.getDbName());
        statement.close();
    }
}
