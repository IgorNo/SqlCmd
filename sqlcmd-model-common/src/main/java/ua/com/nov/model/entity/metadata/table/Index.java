package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.metadata.table.constraint.Key;

import java.util.Collection;
import java.util.Map;

public class Index extends TableMd {
    private final Map<Integer, String> columnList;

    public final static class Builder extends Key.Builder {
        public Builder(TableId tableId, String keyName, String... columnName) {
            super(keyName, tableId, columnName);
        }

        public Builder(String... col) {
            this(null, null, col);
        }

        public Index build() {
            return new Index(this);
        }

        protected Map<Integer, String> getColumnMap() {
            return super.getColumnMap();
        }
    }

    // вложенный класс создатся для обеспечения уникальности ключей
    private static class IndexId extends TableMdId {
        public IndexId(TableId containerId, String name) {
            super(containerId, name);
        }
    }

    public Index(Builder builder) {
        super(new IndexId(builder.getTableId(), builder.getName()));
        this.columnList = builder.getColumnMap();
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
        final StringBuilder sb = new StringBuilder("INDEX ").append(getId().getName()).append(" (");
        String s = "";
        for (int i = 1; i <= getNumberOfColumns(); i++) {
            sb.append(s).append(getColumnName(i));
            if (s.isEmpty()) s = ",";
        }
        return sb.append(')').toString();
    }
}
