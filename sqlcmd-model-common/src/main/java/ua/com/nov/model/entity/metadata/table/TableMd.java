package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.Buildable;
import ua.com.nov.model.entity.metadata.MetaData;
import ua.com.nov.model.entity.metadata.MetaDataId;
import ua.com.nov.model.entity.metadata.database.Database;

public abstract class TableMd<K extends TableMd.Id> extends MetaData<K> {

    public abstract static class Builder<V> implements Buildable<V> {
        private Table.Id tableId;
        private String name;

        public Builder(String name, Table.Id tableId) {
            this.tableId = tableId;
            this.name = name;
        }

        public void setTableId(Table.Id tableId) {
            this.tableId = tableId;
        }

        public Table.Id getTableId() {
            return tableId;
        }

        public String getName() {
            return name;
        }
    }

    public TableMd(K id) {
        super(id, null);
    }

    public Table.Id getTableId() {
        return getId().getContainerId();
    }

    public String getName() {
        return getId().getName();
    }

    public abstract static class Id extends MetaDataId<Table.Id> {

        public Id(Table.Id containerId, String name) {
            super(containerId, name);
        }

        @Override
        public Database getDb() {
            return super.getContainerId().getDb();
        }

        public Table.Id getTableId() {
            return getContainerId();
        }
    }
}
