package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.Column;
import ua.com.nov.model.entity.metadata.table.TableMdId;

import java.util.HashMap;
import java.util.Map;

public abstract class Key extends Constraint {
    private final Map<Integer, Column> key;

    protected static class Builder {
        private final TableMdId id;
        private final Map<Integer, Column> key = new HashMap<>();

        public Builder(TableId tableId, String keyName, Column col) {
            id = new TableMdId(tableId, keyName);
            addColumn(1, col);
        }

        /**
         * Add column to constraint
         *
         * @param keySeq - sequence number within primary constraint( a value of 1 represents the first column of
         *               the foreign constraint, a value of 2 would represent the second column within the primary constraint)
         * @param col
         */
        public Builder addColumn(int keySeq, Column col) {
            if (key.put(keySeq, col) != null) {
                throw new IllegalArgumentException(String.format("Column '%s' already belongs in this key.",
                        col.getId().getFullName()));
            }
            return this;
        }
    }

    protected Key(Builder builder) {
        super(builder.id);
        this.key = builder.key;
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
        final StringBuilder sb = new StringBuilder(super.toString()).append(" %s(");
        String s = "";
        for (Column column : key.values()) {
            sb.append(s).append(column.getName());
            if (s.isEmpty()) s = ",";
        }
        return sb.append(')').toString();
    }
}
