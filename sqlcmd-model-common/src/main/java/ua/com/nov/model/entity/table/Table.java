package ua.com.nov.model.entity.table;

import ua.com.nov.model.dao.SqlStatementSource;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.key.ForeignKey;
import ua.com.nov.model.entity.key.Key;
import ua.com.nov.model.entity.row.RowData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table implements Persistent<TableID, Table> {
    private TableID id;     // table primary key
    private String name;    // table name
    private String type;    // table type.  Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
                            //                                "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
    private String remarks;  // explanatory comment on the table

    private Map<Integer, Column> columns = new HashMap<>(); // all table columns
    private Key primaryKey; // table primary key columns
    private List<Key> uniqueKeyList; // table unique key list
    private List<ForeignKey> foreignKeyList; // table foreign key list
    private List<String> checkExpressionList; // table check expression list
    private List<RowData> rows = new ArrayList<>();   // table data

    private String tableProperies = "";

    public Table(TableID id) {
        this(id, "TABLE");
    }

    public Table(Database db, String catalog, String schema, String name) {
        this(new TableID(db, catalog, schema, name));
    }

    public Table(TableID id, String type) {
        this.id = id;
        this.name = id.getName();
        this.type = type;
    }

    @Override
    public SqlStatementSource<TableID, Table> getSqlStmtSource() {
        return id.getDb().getTableSqlStmtSource();
    }

    public TableID getId() {
        return id;
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
        return columns.get(index).getPk().getName();
    }

    public int getColumnIndex(String columnName) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getPk().equals(columnName))
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
}
