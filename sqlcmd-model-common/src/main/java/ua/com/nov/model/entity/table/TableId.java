package ua.com.nov.model.entity.table;

import ua.com.nov.model.entity.Mappable;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.statement.AbstractSqlTableStatements;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TableId implements Persistent<Table> {
    private final Database db;
    private final String name;    // table name
    private final String catalog; // table catalog
    private final String schema;  // table schema

    private TableRowMapper rowMapper = new TableRowMapper();

    public TableId(Database db, String name, String catalog, String schema) {
        this.db = db;
        this.catalog = catalog;
        this.schema = schema;
        this.name = name;
    }

    public TableId(Database db, String name) {
        this(db, name, null, null);
    }

    @Override
    public AbstractSqlTableStatements getSqlStmtSource() {
        return db.getTableSqlStmtSource();
    }

    @Override
    public Mappable<Table> getRowMapper() {
        return rowMapper;
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

    public class TableRowMapper implements Mappable<Table> {
        @Override
        public Table rowMap(ResultSet rs) throws SQLException {
            TableId tableId = new TableId(db, rs.getString("TABLE_NAME"),
                    rs.getString("TABLE_CAT"), rs.getString("TABLE_SCHEM"));
            if (tableId.equals(this)) {
                return new Table(TableId.this, rs.getString("TABLE_TYPE"));
            } else {
                return new Table(tableId, rs.getString("TABLE_TYPE"));
            }
        }
    }
}
