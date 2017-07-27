package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import ua.com.nov.model.dao.exception.MappingSystemException;

import java.sql.SQLException;

public class PostgreSqlRowDaoTest extends AbstractRowDaoTest {

    @BeforeClass
    public static void setUpClass() throws MappingSystemException, SQLException {
        PostgreSqlTableDaoTest.setUpClass();
        tableDaoTest = new PostgreSqlTableDaoTest();
        AbstractRowDaoTest.setUpClass();
    }
}
