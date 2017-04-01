package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.Table;

public class UniqueKey extends Key<UniqueKey.Id> {

    public final static class Builder extends Key.Builder {

        public Builder(String keyName, Table.Id tableId) {
            super(keyName, tableId);
        }

        public Builder(String... col) {
            super(null, null, col);
        }

        @Override
        public Builder options(String options) {
            super.options(options);
            return this;
        }

        public UniqueKey build() {
            unique(true);
            return new UniqueKey(this);
        }
    }

    public UniqueKey(Builder builder) {
        super(builder, new Id(builder.getTableId(), builder.generateNameIfNull("unique")));
    }

    public static class Id extends Constraint.Id {
        public Id(Table.Id containerId, String name) {
            super(containerId, name);
        }

        @Override
        public String getMdName() {
            return "UNIQUE";
        }
    }

}
