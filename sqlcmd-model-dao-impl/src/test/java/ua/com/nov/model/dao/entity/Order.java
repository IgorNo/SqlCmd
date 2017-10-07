package ua.com.nov.model.dao.entity;

import org.springframework.jdbc.support.KeyHolder;
import ua.com.nov.model.dao.exception.DAOSystemException;
import ua.com.nov.model.dao.impl.AbstractTableDaoTest;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.metadata.table.GenericTable;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

public class Order extends AbstractRow<Order> {
    private static final GenericTable<Order> GENERIC_TABLE = new GenericTable<>(AbstractTableDaoTest.orders, Order.class);

    public Order(Builder builder) {
        super(builder);
        initId(new Id());
    }

    public static GenericTable<Order> getGenericTable() {
        return GENERIC_TABLE;
    }

    public int getOrderId() {
        return getValue("id");
    }

    public LocalDate getDate() {
        return ((Date) getValue("date")).toLocalDate();
    }

    public int qty() {
        return getValue("qty");
    }

    public BigDecimal amount() {
        return getValue("amount");
    }

    public Product getProduct() throws DAOSystemException {
        return (Product) getForeignKeyValue(Product.getGenericTable());
    }

    public Customer getCustomer() throws DAOSystemException {
        return (Customer) getForeignKeyValue(Customer.getGenericTable());
    }

    public static class Builder extends AbstractRow.Builder<Order> {
        public Builder() {
            super(GENERIC_TABLE);
        }

        public Builder(Order order) {
            super(order);
        }

        public Builder id(long id) {
            setValue("id", id);
            return this;
        }

        public Builder id(KeyHolder id) {
            setId(id);
            return this;
        }

        public Builder date(LocalDate date) {
            setValue("date", Date.valueOf(date));
            return this;
        }

        public Builder qty(long qty) {
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

    public static class Id extends AbstractRow.Id {

        public Id(int value) {
            super(GENERIC_TABLE, value);
        }

        public Id() {
            super(GENERIC_TABLE);
        }

    }

}