package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import ua.com.nov.model.dao.exception.DAOSystemException;

import java.sql.SQLException;

public class PostgreSqlRowDaoTest extends AbstractRowDaoTest {

    @BeforeClass
    public static void setUpClass() throws DAOSystemException, SQLException {
        PostgreSqlTableDaoTest.setUpClass();
        tableDaoTest = new PostgreSqlTableDaoTest();
        AbstractRowDaoTest.setUpClass();
    }
}
