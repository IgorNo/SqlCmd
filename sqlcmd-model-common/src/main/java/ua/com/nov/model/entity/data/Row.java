package ua.com.nov.model.entity.data;

import org.springframework.jdbc.support.KeyHolder;
import ua.com.nov.model.entity.metadata.table.Table;

public class Row extends AbstractRow {

    public Row(Builder builder) {
        super(builder);
        initId(new Id(getTable()));
    }

    public static class Builder extends AbstractRow.Builder<Row> {
        public Builder(Table table) {
            super(table);
        }

        public Builder(Row row) {
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
        public Builder setForeignKey(AbstractRow value) {
            super.setForeignKey(value);
            return this;
        }

        @Override
        public Row build() {
            return new Row(this);
        }
    }

    public static class Id extends AbstractRow.Id {
        public Id(Table table, Object... values) {
            super(table, values);
        }
    }
}
