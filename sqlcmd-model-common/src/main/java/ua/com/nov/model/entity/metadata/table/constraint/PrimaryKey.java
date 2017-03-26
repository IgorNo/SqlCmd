package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMdId;

public class PrimaryKey extends Key {

    public final static class Builder extends Key.Builder {

        public Builder(String keyName, TableId tableId) {
            super(keyName, tableId);
        }

        public Builder(TableId tableId, String... column) {
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
            public String getMetaDataName() {
                return "PRIMARY KEY";
            }
        });
    }

}
