package ua.com.nov.model.dao.impl;

import org.junit.*;
import ua.com.nov.model.dao.entity.Customer;
import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.service.CachedRDBMapper;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class CachedRDBMapperTest {
    protected static AbstractRowDaoTest rowTest = new HyperSqlRowDaoTest();

    @BeforeClass
    public static void setUpClass() throws MappingSystemException, SQLException {
        HyperSqlRowDaoTest.setUpClass();
    }

    @AfterClass
    public static void tearDownClass() throws SQLException, MappingSystemException {
        AbstractRowDaoTest.setUpClass();
    }

    @Before
    public void setUp() throws MappingSystemException {
        rowTest.setUp();
    }

    @After
    public void tearDown() throws MappingSystemException {
        rowTest.tearDown();
    }

    @Test
    public void testGetRow() throws MappingSystemException {
        CachedRDBMapper dbMapper = new CachedRDBMapper();
        dbMapper.setDataSource(AbstractTableDaoTest.dataSource);
        Customer customer = AbstractRowDaoTest.customerList.get(0);

        assertTrue(customer.equals(dbMapper.get(customer.getId())));
        assertTrue(customer.equals(dbMapper.get(customer.getId())));
    }
}
