package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.metadata.table.constraint.Constraint;
import ua.com.nov.model.entity.metadata.table.constraint.Key;

public class Index extends Key<Index.Id> {
    private final String indexType;
    private final String using;

    public final static class Builder extends Key.Builder {
        private String indexType;
        private String using;

        public Builder(String keyName, Table.Id tableId) {
            super(keyName, tableId);
            unique(false);
        }

        public Builder(String keyName, Table.Id tableId, String... col) {
            super(keyName, tableId, col);
            unique(false);
        }

        public Builder(String... col) {
            this(null, null, col);
        }

        public Builder indexType(String indexType) {
            this.indexType = indexType;
            return this;
        }

        public Builder using(String using) {
            this.using = using;
            return this;
        }

        @Override
        public Builder unique(boolean unique) {
            super.unique(unique);
            return this;
        }

        @Override
        public Builder options(String options) {
            super.options(options);
            return this;
        }

        public Index build() {
            return new Index(this);
        }
    }

    public static class Id extends Constraint.Id{
        public Id(Table.Id containerId, String name) {
            super(containerId, name);
        }

        @Override
        public String getMdName() {
            return "INDEX";
        }
    }

    public Index(Builder builder) {
        super(builder, new Id(builder.getTableId(), builder.getName()));
        this.indexType = builder.indexType;
        this.using = builder.using;
    }

    public String getIndexType() {
        return indexType;
    }

    public String getUsing() {
        return using;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        if (indexType != null) {
            sb.append(indexType).append(' ');
        } else if (isUnique()) {
            sb.append("UNIQUE ");
        }

        sb.append("INDEX ").append(getId().getName()).append(" ON ").append(getTableId().getFullName()).append(" (");

        String s = "";
        for (int i = 1; i <= getNumberOfColumns(); i++) {
            sb.append(s).append(getColumn(i));
            if (s.isEmpty()) s = ",";
        }
        sb.append(')');

        if (using != null)
            sb.append(" USING ").append(using);
        if (getMdOptions() != null)
            sb.append(' ').append(getMdOptions());

        return sb.toString();
    }

}
