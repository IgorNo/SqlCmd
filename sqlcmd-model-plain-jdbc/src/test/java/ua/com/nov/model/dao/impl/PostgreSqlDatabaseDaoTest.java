package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import ua.com.nov.model.SingleConnectionDataSource;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.Database;
import ua.com.nov.model.util.DataSourceUrl;

import javax.sql.DataSource;
import java.sql.SQLException;

public class PostgreSqlDatabaseDaoTest extends AbstractDataBaseDaoTest{
    public static final DataSource DATA_SOURCE =
            new SingleConnectionDataSource(DataSourceUrl.POSTGRE_SQL_LOCAL,
                                           new Database("postgres", "postgres", "postgres")
            );
    public static final AbstractDao<String, Database> DAO = new PostgreSqlDatabaseDao();
    public static final Database TEST_DATABASE = new Database("tmp", "postgres", "postgres");

    @Override
    public Database getTestDatabase() {
        return TEST_DATABASE;
    }

    @Override
    public DataSource getDataSource() throws SQLException {
        return DATA_SOURCE;
    }

    @Override
    public AbstractDao<String, Database> getDao() {
        return DAO;
    }

    @Override
    public String getDbUrl() {
        return DataSourceUrl.POSTGRE_SQL_LOCAL;
    }

    @AfterClass
    public static void tearDownClass() throws SQLException{
        DATA_SOURCE.getConnection().close();
    }
}
