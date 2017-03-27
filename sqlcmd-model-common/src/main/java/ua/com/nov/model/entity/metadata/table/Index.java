package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.metadata.table.constraint.Key;

public class Index extends Key {
    private final String indexType;
    private final String using;

    public final static class Builder extends Key.Builder {
        private String indexType;
        private String using;

        public Builder(String keyName, TableId tableId) {
            super(keyName, tableId);
            unique(false);
        }

        public Builder(String keyName, TableId tableId, String... col) {
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

    public Index(Builder builder) {
        super(builder, new TableMdId(builder.getTableId(), builder.getName()) {
            @Override
            public String getMdName() {
                return "INDEX";
            }
        });
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
        if (getOptions() != null)
            sb.append(' ').append(getOptions());

        return sb.toString();
    }

}
