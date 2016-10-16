package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.PostgreSqlLocalDataSource;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.DataBase;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PostgreSqlDataBaseDaoTest {

    private static final String DROP_DB_SQL = "DROP DATABASE IF EXISTS ";

    private static DataSource dataSource = new PostgreSqlLocalDataSource(new DataBase("postgres", "postgres", "postgres"));
    private static AbstractDao dao = new PostgreSqlDataBaseDao();
    private static DataBase tmpDataBase = new DataBase("tmp", "postgres", "postgres");

    @Before
    public void setUp() throws SQLException {
        dao.setDataSource(dataSource);
        dao.create(tmpDataBase);
    }

    @Test
    public void testCreateDataBase() throws SQLException {
        DataSource tmpDataSource = new PostgreSqlLocalDataSource(tmpDataBase);
        Connection conn = tmpDataSource.getConnection();
        assertTrue(conn != null);
        conn.close();
    }

    @Test(expected = SQLException.class)
    public void testDeleteDataBase() throws SQLException {
        dao.delete(tmpDataBase);
        DataSource tmpDataSource = new PostgreSqlLocalDataSource(tmpDataBase);
        Connection conn = tmpDataSource.getConnection();
        assertTrue(conn == null);
        conn.close();
    }

    @Test
    public void testReadAll() throws SQLException{
        List<DataBase> dataBaseList = dao.readAll();
        assertTrue(dataBaseList.contains(new DataBase("postgres", "postgres", "postgres")));
        assertTrue(dataBaseList.contains(tmpDataBase));
    }

    @Test
    public void testCount() throws SQLException {
        assertTrue(dao.count() > 0);
    }

    @After
    public void clouseUp() throws SQLException{
        Statement statement = dataSource.getConnection().createStatement();
        statement.executeUpdate(DROP_DB_SQL + tmpDataBase.getDbName());
        statement.close();
    }
}
