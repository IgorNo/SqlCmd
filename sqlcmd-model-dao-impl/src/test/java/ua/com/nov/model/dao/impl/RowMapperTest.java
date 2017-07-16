package ua.com.nov.model.dao.impl;

import org.junit.*;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.service.RowMapper;

import java.sql.SQLException;

public class RowMapperTest {
    protected static AbstractRowDaoTest rowTest = new HyperSqlRowDaoTest();

    @BeforeClass
    public static void setUpClass() throws DaoSystemException, SQLException {
        HyperSqlRowDaoTest.setUpClass();
    }

    @AfterClass
    public static void tearDownClass() throws SQLException, DaoSystemException {
        AbstractRowDaoTest.setUpClass();
    }

    @Before
    public void setUp() throws DaoSystemException {
        rowTest.setUp();
    }

    @After
    public void tearDown() throws DaoSystemException {
        rowTest.tearDown();
    }

    @Test
    public void testGetRow() throws DaoSystemException {
        Customer customer = RowMapper.getInstance().get(new Customer.Id(1),
                Customer.class, AbstractTableDaoTest.dataSource);
    }
}
