package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.database.PostgreSqlIndexOptions;
import ua.com.nov.model.entity.metadata.server.PostgreSqlServer;
import ua.com.nov.model.entity.metadata.table.column.KeyCol;
import ua.com.nov.model.entity.metadata.table.constraint.Key;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Index extends TableMd<Index.Id> {
    private final Map<Integer, KeyCol> columnList;

    public final static class Builder extends Key.Builder<Index> {

        public Builder(String keyName, Table.Id tableId) {
            super(keyName, tableId);
        }

        public Builder(String keyName, Table.Id tableId, String... col) {
            super(keyName, tableId, col);
        }

        public Builder(String... col) {
            this(null, null, col);
        }

        public Builder options(MetaDataOptions<Index> options) {
            super.setOptions(options);
            return this;
        }

        public Index build() {
            if (getName() == null) setName(generateName("idx"));
            return new Index(this);
        }

        @Override
        protected Map<Integer, KeyCol> getColumnMap() {
            return super.getColumnMap();
        }
    }

    public static class Id extends TableMd.Id {
        public Id(Table.Id containerId, String name) {
            super(containerId, name);
        }

        @Override
        public String getMdName() {
            return "INDEX";
        }

        @Override
        public String getFullName() {
            return getName();
        }
    }

    private Index(Builder builder) {
        super(new Id(builder.getTableId(), builder.getName()), builder);
        this.columnList = builder.getColumnMap();
        for (int i = 1; i <= columnList.size(); i++) {
            if (columnList.get(i) == null)
                throw new IllegalArgumentException("Invalid key's structure");
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o.getClass() == Index.class || o instanceof Key)) return false;

        Index idx;
        if (o instanceof Key) idx = ((Key) o).getIndex();
        else idx = (Index) o;

        if (!getId().getTableId().equals(idx.getId().getTableId())) return false;
        return columnList.equals(idx.columnList);
    }

    @Override
    public int hashCode() {
        int result = getTableId().hashCode();
        result = 31 * result + columnList.hashCode();
        return result;
    }

    @Override
    public String getCreateStmtDefinition(String conflictOption) {
        final StringBuilder sb = new StringBuilder(" ON ");
        sb.append(getId().getTableId().getFullName());

        if (getId().getTableId().getServer().getClass().equals(PostgreSqlServer.class)) {
            if (getOptions() != null) {
                PostgreSqlIndexOptions options = (PostgreSqlIndexOptions) getOptions();
                if (options.getUsing() != null) sb.append(options.getUsing());
            }
        }

        sb.append(' ').append(getColumnNames());

        return String.format(super.getCreateStmtDefinition(conflictOption), sb.toString());
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
