package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.Mappable;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.AbstractMetaDataId;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TableId extends AbstractMetaDataId<Database> implements Persistent{
    private final String catalog; // table catalog
    private final String schema;  // table schema

    public TableId(Database db, String name, String catalog, String schema) {
        super(db, name);
        this.catalog = catalog;
        this.schema = schema;
    }

    @Override
    public Database getDb() {
        return getContainerId().getDb();
    }

    public TableId(Database db, String name) {
        this(db, name, null, null);
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
        int result = getDb().hashCode();
        result = 31 * result + getFullName().toLowerCase().hashCode();
        return result;
    }

    public class TableRowMapper implements Mappable {
        @Override
        public Table.Builder rowMap(ResultSet rs) throws SQLException {
            TableId tableId = new TableId(getDb(), rs.getString("TABLE_NAME"),
                    rs.getString("TABLE_CAT"), rs.getString("TABLE_SCHEM"));
            if (tableId.equals(this)) {
                return new Table.Builder(TableId.this, rs.getString("TABLE_TYPE"));
            } else {
                return new Table.Builder(tableId, rs.getString("TABLE_TYPE"));
            }
        }
    }
}
