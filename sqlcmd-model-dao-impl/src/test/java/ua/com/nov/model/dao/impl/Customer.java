package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.data.AbstractRow;

public class Customer extends AbstractRow {

    private Customer(Builder builder) {
        super(builder);
        initId(new Id());
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

    public static class Id extends AbstractRow.Id {

        public Id(int value) {
            super(AbstractTableDaoTest.customers, value);
        }

        public Id() {
            super(AbstractTableDaoTest.customers);
        }
    }
}
