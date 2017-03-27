package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.metadata.AbstractMetaDataId;
import ua.com.nov.model.entity.metadata.database.Database;

public class TableId extends AbstractMetaDataId<Database.Id> {
    public static final String META_DATA_NAME = "TABLE";
    private final String catalog; // table catalog
    private final String schema;  // table schema

    public TableId(Database.Id db, String name, String catalog, String schema) {
        super(db, name);
        this.catalog = catalog;
        this.schema = schema;
    }

    public TableId(Database.Id db, String name) {
        this(db, name, null, null);
    }

    @Override
    public String getMdName() {
        return META_DATA_NAME;
    }

    @Override
    public String getFullName() {
        return getDb().getFullTableName(this);
    }

    public String getCatalog() {
        return getDb().convert(catalog);
    }

    public String getSchema() {
        return getDb().convert(schema);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableId)) return false;

        TableId id = (TableId) o;

        return getFullName().equalsIgnoreCase(id.getFullName());
    }

    @Override
    public int hashCode() {
        int result = getDb().hashCode();
        result = 31 * result + getFullName().toLowerCase().hashCode();
        return result;
    }

}
