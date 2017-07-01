package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import ua.com.nov.model.dao.exception.DaoSystemException;

import java.sql.SQLException;

public class MySqlRowTest extends AbstractRowTest {

    @BeforeClass
    public static void setUpClass() throws DaoSystemException, SQLException {
        MySqlTableDaoTest.setUpClass();
        tableDaoTest = new MySqlTableDaoTest();
        AbstractRowTest.setUpClass();
    }
}
