package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.data.Row;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.entity.metadata.table.Table;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public abstract class AbstractRowTest {
    protected static final RowDao ROW_DAO = new RowDao();

    protected static final AbstractRowDao<Customer> CUSTOMER_DAO = new AbstractRowDao<Customer>() {
        @Override
        protected AbstractRowMapper<Customer, Table> getRowMapper(Table table) {
            return new AbstractRowMapper<Customer, Table>(table) {
                @Override
                public Customer mapRow(ResultSet rs, int i) throws SQLException {
                    Customer.Builder row = new Customer.Builder();
                    setRowValues(rs, row, table);
                    return row.build();
                }
            };
        }
    };


    protected static final AbstractRowDao<Product> PRODUCT_DAO = new AbstractRowDao<Product>() {
        @Override
        protected AbstractRowMapper<Product, Table> getRowMapper(Table table) {
            return new AbstractRowMapper<Product, Table>(table) {
                @Override
                public Product mapRow(ResultSet rs, int i) throws SQLException {
                    Product.Builder row = new Product.Builder();
                    setRowValues(rs, row, table);
                    return row.build();
                }
            };
        }
    };

    protected static final AbstractRowDao<Order> ORDER_DAO = new AbstractRowDao<Order>() {
        @Override
        protected AbstractRowMapper<Order, Table> getRowMapper(Table table) {
            return new AbstractRowMapper<Order, Table>(table) {
                @Override
                public Order mapRow(ResultSet rs, int i) throws SQLException {
                    Order.Builder row = new Order.Builder();
                    setRowValues(rs, row, table);
                    return row.build();
                }
            };
        }
    };

    protected static AbstractTableDaoTest tableDaoTest;

    protected static Server server;

    protected static List<Row> userList;
    protected static List<Customer> customerList;
    protected static List<Product> productList;
    protected static List<Order> orderList;

    protected static void setUpClass() throws DaoSystemException, SQLException {
        tableDaoTest.setUp();
        server = tableDaoTest.testDb.getServer();
        ROW_DAO.setTable(AbstractTableDaoTest.users).setDataSource(tableDaoTest.dataSource);
        CUSTOMER_DAO.setTable(AbstractTableDaoTest.customers).setDataSource(tableDaoTest.dataSource);
        PRODUCT_DAO.setTable(AbstractTableDaoTest.products).setDataSource(tableDaoTest.dataSource);
        ORDER_DAO.setTable(AbstractTableDaoTest.orders).setDataSource(tableDaoTest.dataSource);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException, DaoSystemException {
        tableDaoTest.tearDown();
        ORDER_DAO.getDataSource().getConnection().close();
    }

    @Before
    public void setUp() throws DaoSystemException {

        userList = new ArrayList<>();
        Row.Builder user = new Row.Builder(AbstractTableDaoTest.users).setValue("login", "User1")
                .setValue("password", "1111");
        userList.add(user.setValue("id", (int) ROW_DAO.insert(user.build())).build());

        customerList = new ArrayList<>();
        Customer.Builder customer = new Customer.Builder()
                .name("ООО 'Явир'").phone("313-48-48").address("ул.Покровская, д.7").rating(1000);
        customerList.add(customer.id((int) CUSTOMER_DAO.insert(customer.build())).build());
        customer = new Customer.Builder()
                .name("ООО 'Кускус'").phone("112-14-15").address("ул.Соборная, д.8").rating(1500);
        customerList.add(customer.id((int) CUSTOMER_DAO.insert(customer.build())).build());
        customer = new Customer.Builder()
                .name("Крылов C.C.").phone("444-78-90").address("Зелёный пр-т, д.22").rating(1200);
        customerList.add(customer.id((int) CUSTOMER_DAO.insert(customer.build())).build());

        productList = new ArrayList<>();
        Product.Builder product = new Product.Builder().description("Обогреватель ВГД 121R")
                .details("Инфракрасный обогреватель. 3 режима нагрева:\n" +
                        "400 Вт, 800 Вт, 1200 Вт").price(BigDecimal.valueOf(1145));
        productList.add(product.id((int) PRODUCT_DAO.insert(product.build())).build());
        product = new Product.Builder().description("Гриль СТ-14")
                .details("Мощность 1440 Вт. Быстрый нагрев. Термостат.\n" +
                        "Цветовой индикатор работы").price(BigDecimal.valueOf(2115));
        productList.add(product.id((int) PRODUCT_DAO.insert(product.build())).build());
        product = new Product.Builder().description("Кофеварка ЕКЛ-1032")
                .details("Цвет: черный. Мощность: 450 Вт.\n" +
                        "Вместительность: 2 чашки").price(BigDecimal.valueOf(710));
        productList.add(product.id((int) PRODUCT_DAO.insert(product.build())).build());
        product = new Product.Builder().description("Чайник МН")
                .details("Цвет: белый. Мощность: 2200 Вт. Объем: 2 л").price(BigDecimal.valueOf(925));
        productList.add(product.id((int) PRODUCT_DAO.insert(product.build())).build());
        product = new Product.Builder().description("Утюг c паром АБ 200")
                .details("Цвет: фиолетовый. Мощность: 1400 вт").price(BigDecimal.valueOf(518));
        productList.add(product.id((int) PRODUCT_DAO.insert(product.build())).build());

        orderList = new ArrayList<>();
        Order.Builder order = new Order.Builder().date(LocalDate.of(2017, 05, 10))
                .qty(8).product(productList.get(0)).amount(BigDecimal.valueOf(450000, 2)).customer(customerList.get(0));
        orderList.add(order.id((int) ORDER_DAO.insert(order.build())).build());
        order = new Order.Builder().date(LocalDate.of(2017, 05, 10))
                .qty(4).product(productList.get(1)).amount(BigDecimal.valueOf(750000, 2)).customer(customerList.get(0));
        orderList.add(order.id((int) ORDER_DAO.insert(order.build())).build());
        order = new Order.Builder().date(LocalDate.now())
                .qty(14).product(productList.get(2)).amount(BigDecimal.valueOf(22000, 2)).customer(customerList.get(1));
        orderList.add(order.id((int) ORDER_DAO.insert(order.build())).build());
        order = new Order.Builder().date(LocalDate.now())
                .qty(1).product(productList.get(3)).amount(BigDecimal.valueOf(925, 2)).customer(customerList.get(1));
        orderList.add(order.id((int) ORDER_DAO.insert(order.build())).build());
        order = new Order.Builder().date(LocalDate.now())
                .qty(12).product(productList.get(4)).amount(BigDecimal.valueOf(5750, 2)).customer(customerList.get(2));
        orderList.add(order.id((int) ORDER_DAO.insert(order.build())).build());

    }

    @Test
    public void readRow() throws DaoSystemException {
        for (Row row : userList) {
            Row result = ROW_DAO.read(row.getId());
            assertTrue(row.getValue("id").equals((int) result.getValue("id")));
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
    public void readAllRow() throws DaoSystemException {
        List<Row> users = ROW_DAO.readAll();
        for (Row row : users) {
            assertTrue(users.contains(row));
        }
        List<Customer> customers = CUSTOMER_DAO.readAll();
        for (Customer row : customers) {
            assertTrue(customers.contains(row));
        }
        List<Product> products = PRODUCT_DAO.readAll();
        for (Product row : products) {
            assertTrue(products.contains(row));
        }
        List<Order> orders = ORDER_DAO.readAll();
        for (Order row : orders) {
            assertTrue(orders.contains(row));
        }
    }

    @Test
    public void readNRow() throws DaoSystemException {
        List<Product> products = PRODUCT_DAO.readN(1, 2);
        assertTrue(products.size() == 2);
        assertTrue(products.contains(productList.get(1)));
        assertTrue(products.contains(productList.get(2)));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteRow() throws DaoSystemException {
        ORDER_DAO.delete(orderList.get(0));
        ORDER_DAO.read(orderList.get(0).getId());
        assertTrue(false);
    }

    @Test
    public void updateRow() throws DaoSystemException {
        Customer customer = new Customer.Builder(customerList.get(0)).rating(2000).phone("888-48-48").build();
        CUSTOMER_DAO.update(customer);
        Customer result = CUSTOMER_DAO.read(customer.getId());
        assertTrue(customer.equals(result));
    }

    @Test
    public void countRow() throws DaoSystemException {
        assertTrue(PRODUCT_DAO.count() == 5);
    }

    @After
    public void tearDown() throws DaoSystemException {
        ROW_DAO.deleteAll();
        ORDER_DAO.deleteAll();
        CUSTOMER_DAO.deleteAll();
        PRODUCT_DAO.deleteAll();
    }
}

class Customer extends AbstractRow {
    private Customer(Builder builder) {
        super(builder);
    }

    public int getCustomerId() {
        return (int) getValue("id");
    }

    public int getRating() {
        return (int) getValue("rating");
    }

    public String getName() {
        return (String) getValue("name");
    }

    public String getPhone() {
        return (String) getValue("phone");
    }

    public String getAddress() {
        return (String) getValue("address");
    }

    public static class Builder extends AbstractRow.Builder<Customer> {

        public Builder() {
            super(AbstractTableDaoTest.customers);
        }

        public Builder(Customer customer) {
            super(customer);
        }

        public Builder id(int id) {
            setValue("id", id);
            return this;
        }

        public Builder rating(int rating) {
            setValue("rating", rating);
            return this;
        }

        public Builder name(String name) {
            setValue("name", name);
            return this;
        }

        public Builder phone(String phone) {
            setValue("phone", phone);
            return this;
        }

        public Builder address(String address) {
            setValue("address", address);
            return this;
        }

        @Override
        public Customer build() {
            return new Customer(this);
        }
    }
}

class Product extends AbstractRow {
    public Product(Builder builder) {
        super(builder);
    }

    public int getProductId() {
        return (int) getValue("id");
    }

    public String getDescription() {
        return (String) getValue("description");
    }

    public String getDetails() {
        return (String) getValue("details");
    }

    public BigDecimal getPrice() {
        return (BigDecimal) getValue("price");
    }

    public static class Builder extends AbstractRow.Builder<Product> {
        public Builder() {
            super(AbstractTableDaoTest.products);
        }

        public Builder(Product product) {
            super(product);
        }

        public Builder id(int id) {
            setValue("id", id);
            return this;
        }

        public Builder description(String description) {
            setValue("description", description);
            return this;
        }

        public Builder details(String details) {
            setValue("details", details);
            return this;
        }

        public Builder price(BigDecimal price) {
            setValue("price", price);
            return this;
        }

        @Override
        public Product build() {
            return new Product(this);
        }
    }
}

class Order extends AbstractRow {
    public Order(Builder builder) {
        super(builder);
    }

    public int getOrderId() {
        return (int) getValue("id");
    }

    public LocalDate getDate() {
        return ((Date) getValue("date")).toLocalDate();
    }

    public int qty() {
        return (int) getValue("qty");
    }

    public BigDecimal amount() {
        return (BigDecimal) getValue("amount");
    }

    public Product getProduct() {
        return (Product) getForeignKeyValue(AbstractTableDaoTest.products);
    }

    public Customer getCustomer() {
        return (Customer) getForeignKeyValue(AbstractTableDaoTest.customers);
    }

    public static class Builder extends AbstractRow.Builder<Order> {
        public Builder() {
            super(AbstractTableDaoTest.orders);
        }

        public Builder(Order order) {
            super(order);
        }

        public Builder id(int id) {
            setValue("id", id);
            return this;
        }

        public Builder date(LocalDate date) {
            setValue("date", Date.valueOf(date));
            return this;
        }

        public Builder qty(int qty) {
            setValue("qty", qty);
            return this;
        }

        public Builder amount(BigDecimal amount) {
            setValue("amount", amount);
            return this;
        }

        public Builder product(Product product) {
            setForeignKey(product);
            return this;
        }

        public Builder customer(Customer customer) {
            setForeignKey(customer);
            return this;
        }

        @Override
        public Order build() {
            return new Order(this);
        }
    }
}


