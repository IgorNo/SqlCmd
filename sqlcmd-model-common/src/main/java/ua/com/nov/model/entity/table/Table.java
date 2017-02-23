package ua.com.nov.model.entity.table;

import ua.com.nov.model.dao.SqlStatementSource;
import ua.com.nov.model.entity.Mappable;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.key.ForeignKey;
import ua.com.nov.model.entity.key.Key;
import ua.com.nov.model.entity.row.RowData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table implements Persistent<TableId, Table, Database> {
    private TableId id;     // table primary id
    private String name;    // table name
    private String type;    // table type.  Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
                            //                                "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
    private String remarks;  // explanatory comment on the table

    private Map<Integer, Column> columns = new HashMap<>(); // all table column
    private Key primaryKey; // table primary id column
    private List<Key> uniqueKeyList; // table unique id list
    private List<ForeignKey> foreignKeyList; // table foreign id list
    private List<String> checkExpressionList; // table check expression list
    private List<RowData> rows = new ArrayList<>();   // table data

    private String tableProperies = "";

    private TableRowMapper rowMapper = new TableRowMapper();

    public Table(TableId id) {
        this(id, "TABLE");
    }

    public Table(Database db, String catalog, String schema, String name) {
        this(new TableId(db, catalog, schema, name));
    }

    public Table(TableId id, String type) {
        this.id = id;
        this.name = id.getName();
        this.type = type;
    }

    public TableId getId() {
        return id;
    }

    @Override
    public Database getContainer() {
        return id.getDb();
    }

    @Override
    public SqlStatementSource<Table, Database> getSqlStmtSource() {
        return id.getDb().getTableSqlStmtSource();
    }

    @Override
    public Mappable<Table> getRowMapper() {
        return rowMapper;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public void clearColumns() {
        columns.clear();
    }

    public void addColumn(Column col) {
        if (columns.containsKey(col.getOrdinalPosition())) {
            throw new IllegalArgumentException(String.format("Column with ordinal position %s alredy exists",
                    col.getOrdinalPosition()));
        }
        columns.put(col.getOrdinalPosition(), col);
    }

    public int getNumberOfColumns() {
        return columns.size();
    }

    public Column getColumn(int index) {
        return columns.get(index);
    }

    public String getColumnName(int index) {
        return columns.get(index).getId().getName();
    }

    public int getColumnIndex(String columnName) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getId().equals(columnName))
                return i;
        }
        throw new IllegalArgumentException(String.format("Column %s doesn't exist in table %s", columnName, id.getName()));
    }

    public Key getPrimaryKey() {
        if (primaryKey == null) primaryKey = Key.create();
        return primaryKey;
    }

    public List<Key> getUniqueKeyList() {
        if (uniqueKeyList == null) uniqueKeyList = new ArrayList<>();
        return uniqueKeyList;
    }

    public List<ForeignKey> getForeignKeyList() {
        if (foreignKeyList == null) foreignKeyList = new ArrayList<>();
        return foreignKeyList;
    }

    public List<String> getCheckExpressionList() {
        if (checkExpressionList == null) checkExpressionList = new ArrayList<>();
        return checkExpressionList;
    }

    public String getTableProperies() {
        return tableProperies;
    }

    private class TableRowMapper implements Mappable<Table> {
        @Override
        public Table rowMap(ResultSet rs) throws SQLException {
            TableId tableId = new TableId(getId().getDb(), rs.getString("TABLE_CAT"),
                    rs.getString("TABLE_SCHEM"), rs.getString("TABLE_NAME"));
            return new Table(tableId, rs.getString("TABLE_TYPE"));
        }
    }
}
