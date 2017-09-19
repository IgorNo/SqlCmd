package ua.com.nov.model.dao.impl;

import org.junit.*;
import ua.com.nov.model.dao.entity.Customer;
import ua.com.nov.model.dao.entity.Order;
import ua.com.nov.model.dao.entity.Product;
import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.exception.NoSuchEntityException;
import ua.com.nov.model.dao.service.CachedRDBMapper;
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

public class CachedRDBMapperTest {
    public static GenericTable<Row> users;
    protected static AbstractTableDaoTest tableDaoTest = new MySqlTableDaoTest();
    protected static List<Row> userList;
    protected static List<Customer> customerList;
    protected static List<Product> productList;
    protected static List<Order> orderList;

    protected static CachedRDBMapper<Row> userMapper = new CachedRDBMapper<>(); // raw RowMapper
    protected static CachedRDBMapper<Customer> customerMapper = new CachedRDBMapper<>();
    protected static CachedRDBMapper<Product> productMapper = new CachedRDBMapper<>();
    protected static CachedRDBMapper<Order> orderMapper = new CachedRDBMapper<>();


    @BeforeClass
    public static void setUpClass() throws MappingSystemException, SQLException {
        MySqlTableDaoTest.setUpClass();
        tableDaoTest.setUp();

        // raw table
        users = new RowTable(AbstractTableDaoTest.users);

        userMapper.setDataSource(AbstractTableDaoTest.dataSource);
        userMapper.setTable(users);
        customerMapper.setDataSource(AbstractTableDaoTest.dataSource);
        customerMapper.setTable(Customer.getGenericTable());
        productMapper.setDataSource(AbstractTableDaoTest.dataSource);
        productMapper.setTable(Product.getGenericTable());
        orderMapper.setDataSource(AbstractTableDaoTest.dataSource);
        orderMapper.setTable(Order.getGenericTable());

    }

    @AfterClass
    public static void tearDownClass() throws SQLException, MappingSystemException {
        tableDaoTest.tearDown();
        MySqlTableDaoTest.tearDownClass();
    }

    @Before
    public void setUp() throws MappingSystemException {
        tearDown();
        userList = new ArrayList<>();
        Row user = new Row.Builder(users).setValue("login", "User1")
                .setValue("password", "1111").build();
        userList.add(userMapper.add(user));

        customerList = new ArrayList<>();
        Customer customer = new Customer.Builder()
                .name("ООО 'Явир'").phone("313-48-48").address("ул.Покровская, д.7").rating(1000).build();
        customerList.add(customerMapper.add(customer));
        customer = new Customer.Builder()
                .name("ООО 'Кускус'").phone("112-14-15").address("ул.Соборная, д.8").rating(1500).build();
        customerList.add(customerMapper.add(customer));
        customer = new Customer.Builder()
                .name("Крылов C.C.").phone("444-78-90").address("Зелёный пр-т, д.22").rating(1200).build();
        customerList.add(customerMapper.add(customer));

        productList = new ArrayList<>();
        Product product = new Product.Builder().description("Обогреватель ВГД 121R")
                .details("Инфракрасный обогреватель. 3 режима нагрева:\n" +
                        "400 Вт, 800 Вт, 1200 Вт").price(BigDecimal.valueOf(1145)).build();
        productList.add(productMapper.add(product));
        product = new Product.Builder().description("Гриль СТ-14")
                .details("Мощность 1440 Вт. Быстрый нагрев. Термостат.\n" +
                        "Цветовой индикатор работы").price(BigDecimal.valueOf(2115)).build();
        productList.add(productMapper.add(product));
        product = new Product.Builder().description("Кофеварка ЕКЛ-1032")
                .details("Цвет: черный. Мощность: 450 Вт.\n" +
                        "Вместительность: 2 чашки").price(BigDecimal.valueOf(710)).build();
        productList.add(productMapper.add(product));
        product = new Product.Builder().description("Чайник МН")
                .details("Цвет: белый. Мощность: 2200 Вт. Объем: 2 л").price(BigDecimal.valueOf(925)).build();
        productList.add(productMapper.add(product));
        product = new Product.Builder().description("Утюг c паром АБ 200")
                .details("Цвет: фиолетовый. Мощность: 1400 вт").price(BigDecimal.valueOf(518)).build();
        productList.add(productMapper.add(product));

        orderList = new ArrayList<>();
        Order order = new Order.Builder().date(LocalDate.of(2017, 05, 10))
                .qty(8).product(productList.get(0)).amount(BigDecimal.valueOf(450000, 2))
                .customer(customerList.get(0)).build();
        orderList.add(orderMapper.add(order));
        order = new Order.Builder().date(LocalDate.of(2017, 05, 10))
                .qty(4).product(productList.get(1)).amount(BigDecimal.valueOf(750000, 2))
                .customer(customerList.get(0)).build();
        orderList.add(orderMapper.add(order));
        order = new Order.Builder().date(LocalDate.now())
                .qty(14).product(productList.get(2)).amount(BigDecimal.valueOf(22000, 2))
                .customer(customerList.get(1)).build();
        orderList.add(orderMapper.add(order));
        order = new Order.Builder().date(LocalDate.now())
                .qty(1).product(productList.get(3)).amount(BigDecimal.valueOf(925, 2))
                .customer(customerList.get(1)).build();
        orderList.add(orderMapper.add(order));
        order = new Order.Builder().date(LocalDate.now())
                .qty(12).product(productList.get(4)).amount(BigDecimal.valueOf(5750, 2))
                .customer(customerList.get(2)).build();
        orderList.add(orderMapper.add(order));

        userMapper.clearCache();
    }

    @After
    public void tearDown() throws MappingSystemException {
        userMapper.deleteAll();
        orderMapper.deleteAll();
        productMapper.deleteAll();
        customerMapper.deleteAll();
    }

    @Test
    public void testGetRow() throws MappingSystemException {
        for (Row row : userList) {
            // read from database
            AbstractRow result = userMapper.get(row.getId());
            assertTrue(row.getValue("id").equals(result.getValue("id")));
            assertTrue(row.getValue("login").equals(result.getValue("login")));
            assertTrue(row.getValue("password").equals(result.getValue("password")));
            // read from cache
            result = userMapper.get(row.getId());
            assertTrue(row.getValue("id").equals(result.getValue("id")));
            assertTrue(row.getValue("login").equals(result.getValue("login")));
            assertTrue(row.getValue("password").equals(result.getValue("password")));
        }

        for (Customer customer : customerList) {
            // read from database
            assertTrue(customer.equals(customerMapper.get(customer.getId())));
            // read from cache
            assertTrue(customer.equals(customerMapper.get(customer.getId())));
        }

        for (Product product : productList) {
            assertTrue(product.equals(productMapper.get(product.getId())));
            assertTrue(product.equals(productMapper.get(product.getId())));
        }

        for (Order order : orderList) {
            assertTrue(order.equals(orderMapper.get(order.getId())));
            assertTrue(order.equals(orderMapper.get(order.getId())));
        }
    }

    @Test
    public void testGetAllRow() throws MappingSystemException {
        customerMapper.get(customerList.get(0).getId());
        List<Customer> allCustomers = customerMapper.getAll();
        for (Customer row : customerList) {
            assertTrue(allCustomers.contains(row));
        }
        for (Customer row : customerList) {
            assertTrue(allCustomers.contains(row));
        }
        List<Product> allProducts = productMapper.getAll();
        for (Product row : productList) {
            assertTrue(allProducts.contains(row));
        }
        for (Product row : productList) {
            assertTrue(allProducts.contains(row));
        }
        List<Order> allOrders = orderMapper.getAll();
        for (Order row : orderList) {
            assertTrue(allOrders.contains(row));
        }
        for (Order row : orderList) {
            assertTrue(allOrders.contains(row));
        }
    }

    @Test
    public void testReadNRow() throws MappingSystemException {
        List<Product> products = productMapper.getN(1, 2);
        assertTrue(products.size() == 2);
        assertTrue(products.contains(productList.get(1)));
        assertTrue(products.contains(productList.get(2)));

        productMapper.getAll();
        products = productMapper.getN(1, 2);
        assertTrue(products.size() == 2);
        for (Product product : products) {
            assertTrue(productList.contains(product));
        }
    }

    @Test(expected = NoSuchEntityException.class)
    public void testDeleteRow() throws MappingSystemException {
        orderMapper.delete(orderList.get(0));
        orderMapper.get(orderList.get(0).getId());
        assertTrue(false);
    }

    @Test
    public void testUpdateRow() throws MappingSystemException {
        Customer customer = new Customer.Builder(customerList.get(0)).rating(2000).phone("888-48-48").build();
        customerMapper.change(customerList.get(0), customer);
        Customer result = customerMapper.get(customer.getId());
        assertTrue(customer.equals(result));
    }

    @Test
    public void testCountRow() throws MappingSystemException {
        assertTrue(productMapper.size() == 5);
    }
}
