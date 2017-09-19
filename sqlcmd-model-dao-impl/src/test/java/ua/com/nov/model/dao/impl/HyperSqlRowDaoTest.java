package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import ua.com.nov.model.dao.entity.Customer;
import ua.com.nov.model.dao.entity.Product;
import ua.com.nov.model.dao.exception.MappingSystemException;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class HyperSqlRowDaoTest extends AbstractRowDaoTest {

    @BeforeClass
    public static void setUpClass() throws MappingSystemException, SQLException {
        tableDaoTest = new HyperSqlTableDaoTest();
        HyperSqlTableDaoTest.setUpClass();
        AbstractRowDaoTest.setUpClass();
    }


    public void testReadAllRow() throws MappingSystemException {
        List<Customer> allCustomers = CUSTOMER_DAO.readAll(customerList.get(0).getTable());
        for (Customer row : customerList) {
            assertTrue(allCustomers.contains(row));
        }
        List<Product> allProducts = PRODUCT_DAO.readAll(productList.get(0).getTable());
        for (Product row : productList) {
            assertTrue(allProducts.contains(row));
        }
    }


}
