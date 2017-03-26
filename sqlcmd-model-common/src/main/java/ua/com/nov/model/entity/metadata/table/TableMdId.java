package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.metadata.AbstractMetaDataId;
import ua.com.nov.model.entity.metadata.database.Database;

public abstract class TableMdId extends AbstractMetaDataId<TableId> implements Persistent{

    public TableMdId(TableId containerId, String name) {
        super(containerId, name);
    }

    @Override
    public Database getDb() {
        return super.getContainerId().getDb();
    }

    public TableId getTableId() {
        return getContainerId();
    }
}
