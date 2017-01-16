package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabaseID;
import ua.com.nov.model.entity.database.MySqlDb;
import ua.com.nov.model.util.DataSourceUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

public class MySqlDatabaseDaoTest extends AbstractDatabaseDaoTest {
    public static final String URL = DataSourceUtil.MY_SQL_LOCAL_URL;

    public static final DataSource DATA_SOURCE =
            new SingleConnectionDataSource(new MySqlDb(URL + "sys", "root", "root"));

    public static final AbstractDao<DatabaseID, Database, Object> DAO = new MySqlDatabaseDao();

    public static final Database TEST_DATABASE = new MySqlDb(URL + "tmp", "root", "root");

    @Override
    public Database getTestDatabase() {
        return TEST_DATABASE;
    }

    @Override
    public DataSource getDataSource() throws SQLException {
        return DATA_SOURCE;
    }

    @Override
    public AbstractDao<DatabaseID, Database, Object> getDao() {
        return DAO;
    }

    @AfterClass
    public static void tearDownClass() throws SQLException{
        DATA_SOURCE.getConnection().close();
    }
}
