package ua.com.nov.model.entity.metadata.table.metadata.constraint;

import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.table.metadata.MetaDataId;
import ua.com.nov.model.entity.metadata.table.metadata.column.Column;

import java.util.HashMap;
import java.util.Map;

public class Key implements Unique<MetaDataId> {
    private final MetaDataId id;
    private final Map<Integer, Column> key;

    public static class Builder {
        private final MetaDataId id;
        private final Map<Integer, Column> key = new HashMap<>();

        public Builder(MetaDataId id) {
            this.id = id;
        }

        /**
         * Add column to constraint
         *
         * @param keySeq - sequence number within primary constraint( a value of 1 represents the first column of
         *               the foreign constraint, a value of 2 would represent the second column within the primary constraint)
         * @param column
         */
        public Builder addColumn(int keySeq, Column column) {
            if (key.put(keySeq, column) != null) throw new IllegalArgumentException();
            return this;
        }
    }

    public Key(Builder builder) {
        this.id = builder.id;
        this.key = builder.key;
    }

    @Override
    public MetaDataId getId() {
        return id;
    }

    public int getNumberOfColumns() {
        return key.size();
    }

    public Column getColumn(int keySeq) {
        Column result = key.get(keySeq);
        if (result == null) throw new IllegalArgumentException();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (id.getName() != null) sb.append("CONSTRAINT ").append(id.getName());
        sb.append("key=").append(key);
        sb.append('}');
        return sb.toString();
    }
}
