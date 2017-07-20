package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import ua.com.nov.model.dao.exception.DaoSystemException;

import java.sql.SQLException;

public class HyperSqlRowDaoTest extends AbstractRowDaoTest {

    @BeforeClass
    public static void setUpClass() throws DaoSystemException, SQLException {
        tableDaoTest = new HyperSqlTableDaoTest();
        HyperSqlTableDaoTest.setUpClass();
        AbstractRowDaoTest.setUpClass();
    }

}
