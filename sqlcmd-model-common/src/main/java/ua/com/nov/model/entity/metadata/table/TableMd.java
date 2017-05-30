package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.Buildable;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.Schematic;
import ua.com.nov.model.entity.metadata.MetaData;
import ua.com.nov.model.entity.metadata.MetaDataId;

public abstract class TableMd<I extends TableMd.Id> extends MetaData<I> {

    protected TableMd(I id, Builder builder) {
        super(id, builder.type, builder.options);
    }

    public Table.Id getTableId() {
        return getId().getTableId();
    }

    public abstract static class Builder<V> implements Buildable<V> {
        private Table.Id tableId;
        private String name;
        private String type;
        private MetaDataOptions<? extends MetaData> options;

        public Builder(String name, Table.Id tableId) {
            this.tableId = tableId;
            this.name = name;
        }

        public Table.Id getTableId() {
            return tableId;
        }

        protected void setTableId(Table.Id tableId) {
            this.tableId = tableId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        protected void setType(String type) {
            this.type = type;
        }

        public MetaDataOptions<? extends MetaData> getOptions() {
            return options;
        }

        protected void setOptions(MetaDataOptions<? extends MetaData> options) {
            this.options = options;
        }

        public abstract String generateName(String postfix);
    }

    public abstract static class Id extends MetaDataId<Table.Id> implements Schematic {

        public Id(Table.Id tableId, String name) {
            super(tableId, name);
        }

        public Table.Id getTableId() {
            return getContainerId();
        }

        @Override
        public String getCatalog() {
            return getTableId().getCatalog();
        }

        @Override
        public String getSchema() {
            return getTableId().getSchema();
        }

    }
}
