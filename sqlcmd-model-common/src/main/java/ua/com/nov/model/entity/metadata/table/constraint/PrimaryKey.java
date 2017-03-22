package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.TableId;

public class PrimaryKey extends Key {

    public final static class Builder extends Key.Builder {

        public Builder(String keyName, TableId tableId, String... columnName) {
            super(keyName, tableId, columnName);
        }
        
        public Builder(TableId tableId, String... columnName) {
            this( null, tableId,columnName);
            setName("pkey");
        }

        public Builder(String... columnName) {
            this(null, null, columnName);
        }

        public PrimaryKey build() {
            return new PrimaryKey(this);
        }
    }

    private PrimaryKey(Builder builder) {
        super(builder);
    }

    @Override
    public String toString() {
        return String.format(super.toString(), "PRIMARY KEY");
    }


}
