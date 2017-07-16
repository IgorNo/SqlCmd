package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import ua.com.nov.model.dao.exception.DaoSystemException;

import java.sql.SQLException;

public class HyperSqlRowDaoTest extends AbstractRowDaoTest {

    @BeforeClass
    public static void setUpClass() throws DaoSystemException, SQLException {
        HyperSqlTableDaoTest.setUpClass();
        tableDaoTest = new HyperSqlTableDaoTest();
        AbstractRowDaoTest.setUpClass();
    }

}
