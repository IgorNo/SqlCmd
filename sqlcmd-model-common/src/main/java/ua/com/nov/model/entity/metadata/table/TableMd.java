package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.metadata.AbstractMetaData;

public class TableMd extends AbstractMetaData<TableMdId> {

    public TableMd(TableMdId id) {
        super(id);
    }

    public TableId getTableId() {
        return getId().getContainerId();
    }
}
