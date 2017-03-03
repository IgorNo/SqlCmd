package ua.com.nov.model.entity.metadata.table.metadata;

import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.metadata.AbstractMetaDataId;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.TableId;

public class TableMdId extends AbstractMetaDataId<TableId> implements Persistent{

    public TableMdId(TableId containerId, String name) {
        super(containerId, name);
    }

    @Override
    public Database getDb() {
        return super.getContainerId().getDb();
    }
}
