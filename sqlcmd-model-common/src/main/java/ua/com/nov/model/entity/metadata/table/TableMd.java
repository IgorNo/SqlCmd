package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.metadata.MetaData;

public abstract class TableMd extends MetaData<TableMdId> {

    public static class Builder {
        private Table.Id tableId;
        private String name;

        public Builder(String name, Table.Id tableId) {
            this.tableId = tableId;
            this.name = name;
        }

        public void setTableId(Table.Id tableId) {
            this.tableId = tableId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Table.Id getTableId() {
            return tableId;
        }

        public String getName() {
            return name;
        }
    }

    public TableMd(TableMdId id) {
        super(id, null);
    }

    public Table.Id getTableId() {
        return getId().getContainerId();
    }

    public String getName() {
        return getId().getName();
    }
}
