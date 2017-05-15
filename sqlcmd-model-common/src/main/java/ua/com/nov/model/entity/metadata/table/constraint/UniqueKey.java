package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.Table;

public class UniqueKey extends Key {

    public final static class Builder extends Key.Builder<UniqueKey> {

        public Builder(String keyName, Table.Id tableId) {
            super(keyName, tableId);
        }

        public Builder(String... col) {
            super(null, null, col);
        }

        public Builder options(MetaDataOptions<Index> options) {
            super.setOptions(options);
            return this;
        }

        public UniqueKey build() {
            setType("UNIQUE");
            if (getName() == null) setName(generateName("unique"));
            return new UniqueKey(this);
        }
    }

    private UniqueKey(Builder builder) {
        super(builder);
    }
}
