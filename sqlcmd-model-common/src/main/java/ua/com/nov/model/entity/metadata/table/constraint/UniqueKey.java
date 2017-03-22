package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.TableId;

public class UniqueKey extends Key {

    public final static class Builder extends Key.Builder {

        public Builder(String keyName, TableId tableId, String... col) {
            super(keyName, tableId, col);
        }
        
        public Builder(String... col) {
            this(null, null, col);
        }

        public UniqueKey build() {
            return new UniqueKey(this);
        }
    }

    public UniqueKey(Builder builder) {
        super(builder);
    }

    @Override
    public String toString() {
        return String.format(super.toString(), "UNIQUE");
    }
}
