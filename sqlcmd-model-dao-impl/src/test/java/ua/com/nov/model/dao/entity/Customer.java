package ua.com.nov.model.dao.entity;

import org.springframework.jdbc.support.KeyHolder;
import ua.com.nov.model.dao.impl.AbstractTableDaoTest;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.metadata.table.GenericTable;

public class Customer extends AbstractRow<Customer> {
    private static final GenericTable<Customer> GENERIC_TABLE = new GenericTable<>(AbstractTableDaoTest.customers, Customer.class);

    private Customer(Builder builder) {
        super(builder);
        initId(new Id());
    }

    public static GenericTable<Customer> getGenericTable() {
        return GENERIC_TABLE;
    }

    public int getCustomerId() {
        return getValue("id");
    }

    public int getRating() {
        return getValue("rating");
    }

    public String getName() {
        return getValue("name");
    }

    public String getPhone() {
        return getValue("phone");
    }

    public String getAddress() {
        return getValue("address");
    }

    public static class Builder extends AbstractRow.Builder<Customer> {

        public Builder() {
            super(GENERIC_TABLE);
        }

        public Builder(Customer customer) {
            super(customer);
        }

        public Builder id(long id) {
            setValue("id", id);
            return this;
        }

        public Builder id(KeyHolder id) {
            setId(id);
            return this;
        }

        public Builder rating(long rating) {
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

    private static class Id extends AbstractRow.Id {

        public Id(int value) {
            super(GENERIC_TABLE, value);
        }

        public Id() {
            super(GENERIC_TABLE);
        }

    }
}
