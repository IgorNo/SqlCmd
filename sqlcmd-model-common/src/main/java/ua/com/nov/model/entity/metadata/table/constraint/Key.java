package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.column.KeyCol;

import java.util.*;

public abstract class Key extends Constraint {
    private final Index index;

    public abstract static class Builder<V> extends TableMd.Builder<V> {
        private final Map<Integer, KeyCol> columnList = new TreeMap<>();
        private int keySeq = 1;

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

        public String generateName(String postfix) {
            if (getName() == null) {
                StringBuilder sb = new StringBuilder(getTableId().getName()).append("_");
                for (KeyCol s : getColumnList()) {
                    sb.append(s.getName()).append('_');
                }
                return sb.append(postfix).toString();
            }
            return getName();
        }

        protected Map<Integer, KeyCol> getColumnMap() {
            return columnList;
        }

        protected int getKeySeq() {
            return keySeq;
        }

    }

    protected Key(Builder builder) {
        super(builder);
        Index.Builder indexBuilder = new Index.Builder(builder.getName(), builder.getTableId());
        Set<Map.Entry<Integer, KeyCol>> entry = builder.columnList.entrySet();
        for (Map.Entry<Integer, KeyCol> colEntry : entry) {
            indexBuilder.addColumn(colEntry.getKey(), colEntry.getValue());
        }
        this.index = indexBuilder.build();
    }

    public KeyCol getColumn(int keySeq) {
        return index.getColumn(keySeq);
    }

    public List<String> getColumnsList() {
        return index.getColumnsList();
    }

    public Index getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Key || o.getClass() == Index.class)) return false;

        Index idx;
        if (o instanceof Key) idx = ((Key) o).index;
        else idx = (Index) o;

        return index.equals(idx);
    }

    @Override
    public int hashCode() {
        return index.hashCode();
    }

    @Override
    public String getCreateStmtDefinition(String conflictOption) {
        return String.format(super.getCreateStmtDefinition(conflictOption), index.getColumnNames());
    }

}
