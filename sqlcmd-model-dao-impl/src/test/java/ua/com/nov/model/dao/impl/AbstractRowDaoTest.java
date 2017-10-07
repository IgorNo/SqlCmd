package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.dao.entity.Customer;
import ua.com.nov.model.dao.entity.Order;
import ua.com.nov.model.dao.entity.Product;
import ua.com.nov.model.dao.exception.DAOSystemException;
import ua.com.nov.model.dao.exception.NoSuchEntityException;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.data.Row;
import ua.com.nov.model.entity.metadata.table.GenericTable;
import ua.com.nov.model.entity.metadata.table.RowTable;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public abstract class AbstractRowDaoTest {
    protected static final RowDao<Row> ROW_DAO = new RowDao<>();
    protected static final RowDao<Customer> CUSTOMER_DAO = new RowDao<>();
    protected static final RowDao<Product> PRODUCT_DAO = new RowDao<>();
    protected static final RowDao<Order> ORDER_DAO = new RowDao<>();

    protected static AbstractTableDaoTest tableDaoTest;

    protected static GenericTable<Row> users;

    protected static List<Row> userList;
    protected static List<Customer> customerList;
    protected static List<Product> productList;
    protected static List<Order> orderList;

    public static void setUpClass() throws DAOSystemException, SQLException {
        tableDaoTest.setUp();

        ROW_DAO.setDataSource(tableDaoTest.dataSource);
        CUSTOMER_DAO.setDataSource(tableDaoTest.dataSource);
        PRODUCT_DAO.setDataSource(tableDaoTest.dataSource);
        ORDER_DAO.setDataSource(tableDaoTest.dataSource);

        users = new RowTable(AbstractTableDaoTest.users);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException, DAOSystemException {
        tableDaoTest.tearDown();
        ORDER_DAO.getDataSource().getConnection().close();
    }

    @Before
    public void setUp() throws DAOSystemException {

        userList = new ArrayList<>();
        Row.Builder user = new Row.Builder(users).setValue("login", "User1")
                .setValue("password", "1111");
        userList.add(user.id(ROW_DAO.insert(user.build())).build());

        customerList = new ArrayList<>();
        Customer.Builder customer = new Customer.Builder()
                .name("ООО 'Явир'").phone("313-48-48").address("ул.Покровская, д.7").rating(1000);
        customerList.add(customer.id(CUSTOMER_DAO.insert(customer.build())).build());
        customer = new Customer.Builder()
                .name("ООО 'Кускус'").phone("112-14-15").address("ул.Соборная, д.8").rating(1500);
        customerList.add(customer.id(CUSTOMER_DAO.insert(customer.build())).build());
        customer = new Customer.Builder()
                .name("Крылов C.C.").phone("444-78-90").address("Зелёный пр-т, д.22").rating(1200);
        customerList.add(customer.id(CUSTOMER_DAO.insert(customer.build())).build());

        productList = new ArrayList<>();
        Product.Builder product = new Product.Builder().description("Обогреватель ВГД 121R")
                .details("Инфракрасный обогреватель. 3 режима нагрева:\n" +
                        "400 Вт, 800 Вт, 1200 Вт").price(BigDecimal.valueOf(1145));
        productList.add(product.id(PRODUCT_DAO.insert(product.build())).build());
        product = new Product.Builder().description("Гриль СТ-14")
                .details("Мощность 1440 Вт. Быстрый нагрев. Термостат.\n" +
                        "Цветовой индикатор работы").price(BigDecimal.valueOf(2115));
        productList.add(product.id(PRODUCT_DAO.insert(product.build())).build());
        product = new Product.Builder().description("Кофеварка ЕКЛ-1032")
                .details("Цвет: черный. Мощность: 450 Вт.\n" +
                        "Вместительность: 2 чашки").price(BigDecimal.valueOf(710));
        productList.add(product.id(PRODUCT_DAO.insert(product.build())).build());
        product = new Product.Builder().description("Чайник МН")
                .details("Цвет: белый. Мощность: 2200 Вт. Объем: 2 л").price(BigDecimal.valueOf(925));
        productList.add(product.id(PRODUCT_DAO.insert(product.build())).build());
        product = new Product.Builder().description("Утюг c паром АБ 200")
                .details("Цвет: фиолетовый. Мощность: 1400 вт").price(BigDecimal.valueOf(518));
        productList.add(product.id(PRODUCT_DAO.insert(product.build())).build());

        orderList = new ArrayList<>();
        Order.Builder order = new Order.Builder().date(LocalDate.of(2017, 05, 10))
                .qty(8).product(productList.get(0)).amount(BigDecimal.valueOf(450000, 2))
                .customer(customerList.get(0));
        orderList.add(order.id(ORDER_DAO.insert(order.build())).build());
        order = new Order.Builder().date(LocalDate.of(2017, 05, 10))
                .qty(4).product(productList.get(1)).amount(BigDecimal.valueOf(750000, 2)).customer(customerList.get(0));
        orderList.add(order.id(ORDER_DAO.insert(order.build())).build());
        order = new Order.Builder().date(LocalDate.now())
                .qty(14).product(productList.get(2)).amount(BigDecimal.valueOf(22000, 2)).customer(customerList.get(1));
        orderList.add(order.id(ORDER_DAO.insert(order.build())).build());
        order = new Order.Builder().date(LocalDate.now())
                .qty(1).product(productList.get(3)).amount(BigDecimal.valueOf(925, 2)).customer(customerList.get(1));
        orderList.add(order.id(ORDER_DAO.insert(order.build())).build());
        order = new Order.Builder().date(LocalDate.now())
                .qty(12).product(productList.get(4)).amount(BigDecimal.valueOf(5750, 2)).customer(customerList.get(2));
        orderList.add(order.id(ORDER_DAO.insert(order.build())).build());

    }

    @Test
    public void testReadRow() throws DAOSystemException {
        for (Row row : userList) {
            AbstractRow result = ROW_DAO.read(row.getId());
            assertTrue(row.getValue("id").equals(result.getValue("id")));
            assertTrue(row.getValue("login").equals(result.getValue("login")));
            assertTrue(row.getValue("password").equals(result.getValue("password")));
        }
        for (Customer customer : customerList) {
            Customer result = CUSTOMER_DAO.read(customer.getId());
            assertTrue(customer.equals(result));
        }
        for (Product product : productList) {
            Product result = PRODUCT_DAO.read(product.getId());
            assertTrue(product.equals(result));
        }
        for (Order order : orderList) {
            Order result = ORDER_DAO.read(order.getId());
            Order.Builder builder = new Order.Builder(result).customer(order.getCustomer()).product(order.getProduct());
            assertTrue(order.equals(builder.build()));
        }
    }

    @Test
    public void testReadAllRow() throws DAOSystemException {
        List<Customer> allCustomers = CUSTOMER_DAO.readAll(customerList.get(0).getTable());
        for (Customer row : customerList) {
            assertTrue(allCustomers.contains(row));
        }
        List<Product> allProducts = PRODUCT_DAO.readAll(productList.get(0).getTable());
        for (Product row : productList) {
            assertTrue(allProducts.contains(row));
        }
        List<Order> allOrders = ORDER_DAO.readAll(orderList.get(0).getTable());
        for (Order row : orderList) {
            assertTrue(allOrders.contains(row));
        }
    }

    @Test
    public void testReadNRow() throws DAOSystemException {
        List<Product> products = PRODUCT_DAO.readN(productList.get(0).getTable(), 1, 2);
        assertTrue(products.size() == 2);
        assertTrue(products.contains(AbstractRowDaoTest.productList.get(1)));
        assertTrue(products.contains(AbstractRowDaoTest.productList.get(2)));
    }

    @Test(expected = NoSuchEntityException.class)
    public void testDeleteRow() throws DAOSystemException {
        ORDER_DAO.delete(orderList.get(0).getId());
        ORDER_DAO.read(orderList.get(0).getId());
        assertTrue(false);
    }

    @Test
    public void testUpdateRow() throws DAOSystemException {
        Customer customer = new Customer.Builder(customerList.get(0)).rating(2000).phone("888-48-48").build();
        CUSTOMER_DAO.update(customer);
        Customer result = CUSTOMER_DAO.read(customer.getId());
        assertTrue(customer.equals(result));
    }

    @Test
    public void testCountRow() throws DAOSystemException {
        assertTrue(PRODUCT_DAO.count(productList.get(0).getTable()) == 5);
    }

    @After
    public void tearDown() throws DAOSystemException {
        ROW_DAO.deleteAll(AbstractTableDaoTest.users);
        ROW_DAO.deleteAll(orderList.get(0).getTable());
        ROW_DAO.deleteAll(productList.get(0).getTable());
        ROW_DAO.deleteAll(customerList.get(0).getTable());
    }
}


