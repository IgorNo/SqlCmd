package ua.com.nov.model.entity.metadata.table.metadata.constraint;

import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.metadata.Column;
import ua.com.nov.model.entity.metadata.table.metadata.TableMdId;

public class PrimaryKey extends Key {

    public static class Builder extends Key.Builder {
        public Builder(TableMdId id) {
            super(id);
        }

        public Builder(TableId tableId, String keyName, Column col) {
            super(tableId, keyName, col);
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
