package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.Column;
import ua.com.nov.model.entity.metadata.table.TableId;

public class UniqueKey extends Key {

    public static class Builder extends Key.Builder {

        public Builder(TableId tableId, String keyName, Column col) {
            super(tableId, keyName, col);
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
        return String.format(super.toString(), "UNIQUE KEY");
    }
}
