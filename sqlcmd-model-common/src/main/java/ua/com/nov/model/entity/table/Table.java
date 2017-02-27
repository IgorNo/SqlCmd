package ua.com.nov.model.entity.table;

import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.key.ForeignKey;
import ua.com.nov.model.entity.key.Key;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table implements Unique {
    private final TableId id;     // table primary id
    private String name;    // This field uses for renaming table
    private final String type;    // table type.  Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
                            //                                "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
    private String remarks;  // explanatory comment on the table

    private Map<Integer, Column> columns = new HashMap<>(); // all table column
    private Key primaryKey; // table primary id column
    private List<Key> uniqueKeyList; // table unique id list
    private List<ForeignKey> foreignKeyList; // table foreign id list
    private List<String> checkExpressionList; // table check expression list

    private String tableProperies = "";

    public Table(TableId id) {
        this(id, "TABLE");
    }

    public Table(Database db, String name, String catalog, String schema) {
        this(new TableId(db, name, catalog, schema));
    }

    public Table(TableId id, String type) {
        this.id = id;
        this.name = id.getName();
        this.type = type;
    }

    public TableId getId() {
        return id;
    }

    public Database getDb() {
        return id.getDb();
    }

    public String getCatalog() {
        return id.getCatalog();
    }

    public String getSchema() {
        return id.getSchema();
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return id.getFullName();
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

    public Column getColumn(String columnName) {
        return getColumn(getColumnIndex(columnName));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Table)) return false;

        Table table = (Table) o;

        return id.equals(table.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
