package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMd;

import java.util.*;

public abstract class Key extends Constraint {
    private final Map<Integer, String> columnList;

    public static class Builder extends TableMd.Builder {
        private final Map<Integer, String> columnList = new TreeMap<>();
        private int keySeq = 1;

        public Builder(String keyName, TableId tableId, String... columnNames) {
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
            if (columnList.put(keySeq, columnName.toLowerCase()) != null) {
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

        public void setName(String postfix) {
            if (getName() == null) {
                StringBuilder sb = new StringBuilder(getTableId().getName()).append("_");
                for (String s : getColumnNameList()) {
                    sb.append(s);
                }
                super.setName(sb.append(postfix).toString());
            }
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
        for (int i = 1; i <= columnList.size(); i++) {
            if (columnList.get(i) == null)
                throw new IllegalArgumentException("Invalid key's structure");
        }
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Key key = (Key) o;

        if (!getTableId().equals(key.getTableId())) return false;
        return columnList.equals(key.columnList);
    }

    @Override
    public int hashCode() {
        int result = getTableId().hashCode();
        result = 31 * result + columnList.hashCode();
        return result;
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
