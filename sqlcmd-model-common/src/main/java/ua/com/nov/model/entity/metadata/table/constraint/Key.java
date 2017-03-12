package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMd;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Key extends Constraint {
    private final Map<Integer, String> columnList;

    public static class Builder extends TableMd.Builder {
        private final Map<Integer, String> columnList = new HashMap<>();
        private int keySeq = 1;

        public Builder(TableId tableId, String keyName, String... columnNames) {
            super(tableId, keyName);
            for (String columnName : columnNames) {
                addColumn(columnName);
            }
        }

        /**
         * Add column to constraint
         *
         * @param keySeq - sequence number within primary constraint( a value of 1 represents the first column of
         *               the foreign constraint, a value of 2 would represent the second column within the primary constraint)
         * @param columnName
         */
        public Builder addColumn(int keySeq, String columnName) {
            if (columnList.put(keySeq, columnName) != null) {
                throw new IllegalArgumentException(String.format("Column '%s' already belongs this key.", columnName));
            }
            return this;
        }

        public Builder addColumn(String columnName) {
            addColumn(keySeq++, columnName);
            return this;
        }

        public Collection<String> getColumnNameList() {
            return Collections.unmodifiableCollection(columnList.values());
        }

        protected Map<Integer,String> getColumnMap() {
            return columnList;
        }

        protected int getKeySeq() {
            return keySeq;
        }
    }

    protected Key(Builder builder) {
        super(builder);
        this.columnList = builder.columnList;
    }

    public int getNumberOfColumns() {
        return columnList.size();
    }

    public String getColumnName(int keySeq) {
        String result = columnList.get(keySeq);
        if (result == null) throw new IllegalArgumentException();
        return result;
    }

    public Collection<String> getColumnNames() {
        return columnList.values();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString()).append(" %s(");
        String s = "";
        for (String column : columnList.values()) {
            sb.append(s).append(column);
            if (s.isEmpty()) s = ",";
        }
        return sb.append(')').toString();
    }
}
