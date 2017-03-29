package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.metadata.MetaDataId;
import ua.com.nov.model.entity.metadata.database.Database;

public abstract class TableMdId extends MetaDataId<Table.Id> {

    public TableMdId(Table.Id containerId, String name) {
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
