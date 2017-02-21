package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.BaseDao;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabaseID;
import ua.com.nov.model.entity.database.MySqlDb;
import ua.com.nov.model.util.DbUtil;

import javax.sql.DataSource;

public class MySqlDatabaseDaoTest extends AbstractDatabaseDaoTest {
    public static final String URL = DbUtil.MY_SQL_LOCAL_URL;

    public static final DataSource DATA_SOURCE = DbUtil.MY_SQL_LOCAL_SYSTEM_DB;

    public static final Database TEST_DATABASE = new MySqlDb(URL + "tmp", "root", "root");

    @Override
    public Database getTestDatabase() {
        return TEST_DATABASE;
    }

    @Override
    public DataSource getDataSourceDB() {
        return DATA_SOURCE;
    }

}
