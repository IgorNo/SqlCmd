package ua.com.nov.model.entity.table;

import ua.com.nov.model.entity.database.Database;

public class TablePK {
    private Database db;
    private String catalog; // table catalog
    private String schema;  // table schema
    private String name;    // table name

    public TablePK(Database db, String catalog, String schema, String name) {
        this.db = db;
        this.catalog = catalog;
        this.schema = schema;
        this.name = name;
    }

    public Database getDb() {
        return db;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TablePK tablePK = (TablePK) o;

        if (catalog != null ? !catalog.equals(tablePK.catalog) : tablePK.catalog != null) return false;
        if (!schema.equals(tablePK.schema)) return false;
        return name.equals(tablePK.name);
    }

    @Override
    public int hashCode() {
        int result = catalog != null ? catalog.hashCode() : 0;
        result = 31 * result + schema.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
