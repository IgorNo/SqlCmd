package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.metadata.AbstractMetaData;

public abstract class TableMd extends AbstractMetaData<TableMdId> {

    public static class Builder {
        private TableId tableId;
        private String name;

        public Builder(String name, TableId tableId) {
            this.tableId = tableId;
            this.name = name;
        }

        public void setTableId(TableId tableId) {
            this.tableId = tableId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public TableId getTableId() {
            return tableId;
        }

        public String getName() {
            return name;
        }
    }

    public TableMd(TableMdId id) {
        super(id);
    }

    public TableId getTableId() {
        return getId().getContainerId();
    }

    public String getName() {
        return getId().getName();
    }
}
