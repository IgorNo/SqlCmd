package ua.com.nov.model.entity.data;

import org.springframework.jdbc.support.KeyHolder;
import ua.com.nov.model.entity.metadata.table.GenericTable;

public class Row extends AbstractRow<Row> {

    public Row(Builder builder) {
        super(builder);
        initId(new Id(getTable()));
    }

    public static class Builder extends AbstractRow.Builder<Row> {
        public Builder(GenericTable<Row> table) {
            super(table);
        }

        public Builder(AbstractRow row) {
            super(row);
        }

        public Builder id(KeyHolder id) {
            super.setId(id);
            return this;
        }

        @Override
        public Builder setValue(String column, Object value) {
            super.setValue(column, value);
            return this;
        }

        @Override
        public Builder setForeignKey(AbstractRow row) {
            super.setForeignKey(row);
            return this;
        }

        @Override
        public Row build() {
            return new Row(this);
        }
    }

    public static class Id extends AbstractRow.Id<Row> {

        public Id(GenericTable<Row> table, Object... values) {
            super(table, values);
        }

    }
}
