package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMdId;

public class PrimaryKey extends Key {

    public final static class Builder extends Key.Builder {

        public Builder(String keyName, Table.Id tableId) {
            super(keyName, tableId);
        }

        public Builder(Table.Id tableId, String... column) {
            super( null, tableId,column);
            setName("pkey");
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
        super(builder, new TableMdId(builder.getTableId(), builder.getName()) {
            @Override
            public String getMdName() {
                return "PRIMARY KEY";
            }
        });
    }

}
