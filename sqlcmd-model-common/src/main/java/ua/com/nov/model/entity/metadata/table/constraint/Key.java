package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.TableMdId;
import ua.com.nov.model.entity.metadata.table.column.KeyCol;

import java.util.*;

public abstract class Key extends Constraint {
    private final Map<Integer, KeyCol> columnList;
    private final boolean unique;

    public abstract static class Builder extends TableMd.Builder {
        private final Map<Integer, KeyCol> columnList = new TreeMap<>();
        private int keySeq = 1;
        private boolean unique = true;
        private String options;

        public Builder(String keyName, Table.Id tableId) {
            super(keyName, tableId);
       }

        public Builder(String keyName, Table.Id tableId, KeyCol... columns) {
            this(keyName, tableId);
            for (KeyCol column : columns) {
                addColumn(column);
            }
        }

        public Builder(String keyName, Table.Id tableId, String... columns) {
            this(keyName, tableId);
            for (String column : columns) {
                addColumn(column);
            }
        }

        /**
         * Add column to constraint
         *
         * @param keySeq - sequence number within primary constraint( a value of 1 represents the first column of
         *               the foreign constraint, a value of 2 would represent the second column within the primary constraint)
         * @param column
         */
        public Builder addColumn(int keySeq, KeyCol column) {
            if (columnList.put(keySeq, column) != null) {
                throw new IllegalArgumentException(String.format("Column '%s' already belongs this key.", column));
            }
            return this;
        }

        public Builder addColumn(int keySeq, String column) {
            if (columnList.put(keySeq, new KeyCol(column)) != null) {
                throw new IllegalArgumentException(String.format("Column '%s' already belongs this key.", column));
            }
            return this;
        }

        public Builder addColumn(KeyCol column) {
            addColumn(keySeq++, column);
            return this;
        }

        public Builder addColumn(String column) {
            addColumn(keySeq++, new KeyCol(column));
            return this;
        }

        public Collection<KeyCol> getColumnList() {
            return Collections.unmodifiableCollection(columnList.values());
        }

        public void setName(String postfix) {
            if (getName() == null) {
                StringBuilder sb = new StringBuilder(getTableId().getName()).append("_");
                for (KeyCol s : getColumnList()) {
                    sb.append(s.getName()).append('_');
                }
                super.setName(sb.append(postfix).toString());
            }
        }

        protected Map<Integer, KeyCol> getColumnMap() {
            return columnList;
        }

        protected int getKeySeq() {
            return keySeq;
        }

        protected Builder unique(boolean unique) {
            this.unique = unique;
            return this;
        }

        public Builder options(String options) {
            this.options = options;
            return this;
        }

        public abstract Key build();
    }

    protected Key(Builder builder, TableMdId id) {
        super(id);
        this.columnList = builder.columnList;
        for (int i = 1; i <= columnList.size(); i++) {
            if (columnList.get(i) == null)
                throw new IllegalArgumentException("Invalid key's structure");
        }
        this.unique = builder.unique;
    }

    public int getNumberOfColumns() {
        return columnList.size();
    }

    public KeyCol getColumn(int keySeq) {
        KeyCol result = columnList.get(keySeq);
        if (result == null) throw new IllegalArgumentException();
        return result;
    }

    public List<String> getColumnsList() {
        List<String> result = new ArrayList<>();
        for (KeyCol col : columnList.values()) {
            result.add(col.getName());
        }
        return result;
    }

    public boolean isUnique() {
        return unique;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Key)) return false;

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
        final StringBuilder sb = new StringBuilder(super.toString());

        sb.append(getColumnNames());

        if (getMdOptions() != null)
            sb.append(' ').append(getMdOptions());

        return sb.toString();
    }

    public String getColumnNames() {
        StringBuilder sb = new StringBuilder("(");
        String s = "";
        for (KeyCol column : columnList.values()) {
            sb.append(s).append(column);
            if (s.isEmpty()) s = ",";
        }
        sb.append(')');
        return sb.toString();
    }
}
