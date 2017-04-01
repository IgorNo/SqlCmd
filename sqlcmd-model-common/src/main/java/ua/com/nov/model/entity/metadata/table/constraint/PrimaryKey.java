package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.Table;

public class PrimaryKey extends Key<PrimaryKey.Id> {

    public final static class Builder extends Key.Builder {

        public Builder(String keyName, Table.Id tableId) {
            super(keyName, tableId);
        }

        public Builder(Table.Id tableId, String... column) {
            super( null, tableId,column);
            generateNameIfNull("pkey");
        }

        public Builder(String... columnName) {
            super(null, null, columnName);
        }

        @Override
        public Builder options(String options) {
            super.options(options);
            return this;
        }

        public PrimaryKey build() {
            return new PrimaryKey(this);
        }
    }

    private PrimaryKey(Builder builder) {
        super(builder, new Id(builder.getTableId(), builder.generateNameIfNull("pkey")));
    }

    public static class Id extends Constraint.Id {
        public Id(Table.Id containerId, String name) {
            super(containerId, name);
        }

        @Override
        public String getMdName() {
            return "PRIMARY KEY";
        }

    }
}
