package ua.com.nov.model.dao.entity;

import org.springframework.jdbc.support.KeyHolder;
import ua.com.nov.model.dao.impl.AbstractRowDaoTest;
import ua.com.nov.model.entity.data.AbstractRow;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import static ua.com.nov.model.dao.impl.AbstractRowDaoTest.products;

public class Order extends AbstractRow {
    public Order(Builder builder) {
        super(builder);
        initId(new Id());
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
        return (Product) getForeignKeyValue(products);
    }

    public Customer getCustomer() {
        return (Customer) getForeignKeyValue(AbstractRowDaoTest.customers);
    }

    public static class Builder extends AbstractRow.Builder<Order> {
        public Builder() {
            super(AbstractRowDaoTest.orders);
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
            super(AbstractRowDaoTest.orders, value);
        }

        public Id() {
            super(AbstractRowDaoTest.orders);
        }

    }

}