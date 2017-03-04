package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.metadata.table.constraint.Key;

public class Index extends Key {

    public static class Builder extends Key.Builder {

        public Builder(TableId tableId, String keyName, Column col) {
            super(tableId, keyName, col);
        }

        public Index build() {
            return new Index(this);
        }
    }

    public Index(Builder builder) {
        super(builder);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("INDEX ").append(getId().getName()).append(" (");
        String s = "";
        for (int i = 1; i <= getNumberOfColumns(); i++) {
            sb.append(s).append(getColumn(i).getName());
            if (s.isEmpty()) s = ",";
        }
        return sb.append(')').toString();
    }
}
