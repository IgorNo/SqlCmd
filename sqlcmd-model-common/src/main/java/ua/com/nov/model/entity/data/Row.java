package ua.com.nov.model.entity.data;

import ua.com.nov.model.entity.metadata.table.Table;

public class Row extends AbstractRow {
    private Row(Builder builder) {
        super(builder);
    }

    public static class Builder extends AbstractRow.Builder<Row> {
        public Builder(Table table) {
            super(table);
        }

        public Builder(Row row) {
            super(row);
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
}
