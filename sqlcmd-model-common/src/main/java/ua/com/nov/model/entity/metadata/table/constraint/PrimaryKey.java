package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.Column;
import ua.com.nov.model.entity.metadata.table.TableId;

public class PrimaryKey extends Key {

    public final static class Builder extends Key.Builder {

        public Builder(TableId tableId, String keyName, String columnName) {
            super(tableId, keyName, columnName);
        }
        
        public Builder(String columnName) {
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
