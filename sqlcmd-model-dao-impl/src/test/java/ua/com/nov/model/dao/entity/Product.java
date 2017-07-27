package ua.com.nov.model.dao.entity;

import org.springframework.jdbc.support.KeyHolder;
import ua.com.nov.model.dao.impl.AbstractRowDaoTest;
import ua.com.nov.model.entity.data.AbstractRow;

import java.math.BigDecimal;

public class Product extends AbstractRow {
    public Product(Builder builder) {
        super(builder);
        initId(new Id());
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
            super(AbstractRowDaoTest.products);
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

    public static class Id extends AbstractRow.Id {

        public Id(int value) {
            super(AbstractRowDaoTest.products, value);
        }

        public Id() {
            super(AbstractRowDaoTest.products);
        }

    }
}
