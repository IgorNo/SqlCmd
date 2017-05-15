package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.Table;

public class PrimaryKey extends Key{

    public final static class Builder extends Key.Builder<PrimaryKey> {

        public Builder(String keyName, Table.Id tableId) {
            super(keyName, tableId);
        }

        public Builder(Table.Id tableId, String... column) {
            super( null, tableId,column);
        }

        public Builder(String... columnName) {
            super(null, null, columnName);
        }

        public Builder options(MetaDataOptions<Index> options) {
            super.setOptions(options);
            return this;
        }

        public PrimaryKey build() {
            setType("PRIMARY KEY");
            if (getName() == null) setName(generateName("pkey"));
            return new PrimaryKey(this);
        }
    }

    private PrimaryKey(Builder builder) {
        super(builder);
    }

}
