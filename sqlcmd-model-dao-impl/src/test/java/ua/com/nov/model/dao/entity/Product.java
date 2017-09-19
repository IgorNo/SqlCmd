package ua.com.nov.model.dao.entity;

import org.springframework.jdbc.support.KeyHolder;
import ua.com.nov.model.dao.impl.AbstractTableDaoTest;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.metadata.table.GenericTable;

import java.math.BigDecimal;

public class Product extends AbstractRow<Product> {
    private static final GenericTable<Product> GENERIC_TABLE = new GenericTable<>(AbstractTableDaoTest.products, Product.class);

    public Product(Builder builder) {
        super(builder);
        initId(new Id());
    }

    public static GenericTable<Product> getGenericTable() {
        return GENERIC_TABLE;
    }

    public int getProductId() {
        return getValue("id");
    }

    public String getDescription() {
        return getValue("description");
    }

    public String getDetails() {
        return getValue("details");
    }

    public BigDecimal getPrice() {
        return getValue("price");
    }

    public static class Builder extends AbstractRow.Builder<Product> {
        public Builder() {
            super(GENERIC_TABLE);
        }

        public Builder(Product product) {
            super(product);
        }

        public Builder id(long id) {
            setValue("id", id);
            return this;
        }

        public Builder id(KeyHolder id) {
            setId(id);
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

    private static class Id extends AbstractRow.Id {

        public Id(int value) {
            super(GENERIC_TABLE, value);
        }

        public Id() {
            super(GENERIC_TABLE);
        }

    }
}
