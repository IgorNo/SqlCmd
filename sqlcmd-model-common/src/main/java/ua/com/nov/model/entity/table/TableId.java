package ua.com.nov.model.entity.table;

import ua.com.nov.model.entity.database.Database;

public class TableId {
    private Database db;
    private String name;    // table name
    private String catalog; // table catalog
    private String schema;  // table schema

    public TableId(Database db, String name, String catalog, String schema) {
        this.db = db;
        this.catalog = catalog;
        this.schema = schema;
        this.name = name;
    }

    public TableId(Database db, String name) {
        this(db, name, null, null);
    }

    public Database getDb() {
        return db;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return getDb().getFullTableName(this);
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
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
        int result = db.hashCode();
        result = 31 * result + getFullName().toLowerCase().hashCode();
        return result;
    }
}
