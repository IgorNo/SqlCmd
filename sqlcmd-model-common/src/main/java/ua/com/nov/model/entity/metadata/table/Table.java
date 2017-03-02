package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.Building;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.AbstractMetaData;
import ua.com.nov.model.entity.metadata.table.metadata.column.Column;
import ua.com.nov.model.entity.metadata.table.metadata.constraint.Check;
import ua.com.nov.model.entity.metadata.table.metadata.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.metadata.constraint.Key;
import ua.com.nov.model.entity.metadata.database.Database;

import java.util.*;

public class Table extends AbstractMetaData<TableId> {
    private final String type;    // table type.  Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
                            //                                "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
    private final String remarks;  // explanatory comment on the table

    private final Map<String, Column> columns; // all table column
    private final Key primaryKey; // table primary id column
    private final List<Key> uniqueKeyList; // table unique id list
    private final List<ForeignKey> foreignKeyList; // table foreign id list
    private final List<Check> checkExpressionList; // table check expression list

    private String tableProperties = "";

    public static class Builder implements Building, Unique<TableId> {
        private final TableId id;     // table object identifier
        private final String type;    // table type.  Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
        //                                "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
        private String remarks;  // explanatory comment on the table

        private Map<String, Column> columns = new HashMap<>(); // all table column
        private Key primaryKey; // table primary id column
        private List<Key> uniqueKeyList = new LinkedList<>(); // table unique id list
        private List<ForeignKey> foreignKeyList = new LinkedList<>(); // table foreign id list
        private List<Check> checkExpressionList = new LinkedList<>(); // table check expression list

        private String tableProperties = "";

        public Builder(TableId id) {
            this(id, "TABLE");
        }

        public Builder(Database db, String name, String catalog, String schema) {
            this(new TableId(db, name, catalog, schema));
        }

        public Builder(TableId id, String type) {
            this.id = id;
            this.type = type;
        }

        @Override
        public TableId getId() {
            return id;
        }

        public Builder remarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public Builder columns(Map<String, Column> columns) {
            this.columns = columns;
            return this;
        }

        public Builder addColumn(Column col) {
            if (columns.containsKey(col.getOrdinalPosition())) {
                throw new IllegalArgumentException(String.format("Column with ordinal position %s alredy exists",
                        col.getOrdinalPosition()));
            }
            columns.put(col.getName(), col);
            return this;
        }

        public Builder primaryKey(Key primaryKey) {
            this.primaryKey = primaryKey;
            return this;
        }

        public Builder uniqueKeyList(List<Key> uniqueKeyList) {
            this.uniqueKeyList = uniqueKeyList;
            return this;
        }

        public Builder addUniqueKey(Key uniqueKey) {
            this.uniqueKeyList.add(uniqueKey);
            return this;
        }

        public Builder foreignKeyList(List<ForeignKey> foreignKeyList) {
            this.foreignKeyList = foreignKeyList;
            return this;
        }

        public Builder addForeignKey(ForeignKey foreignKey) {
            this.foreignKeyList.add(foreignKey);
            return this;
        }

        public Builder checkExpressionList(List<Check> checkExpressionList) {
            this.checkExpressionList = checkExpressionList;
            return this;
        }

        public Builder addCheckExpression(Check checkExpression) {
            checkExpressionList.add(checkExpression);
            return this;
        }

        public Builder tableProperties(String tableProperties) {
            this.tableProperties = tableProperties;
            return this;
        }

        public Table build() {
            return new Table(this);
        }
    }

    public Table(Builder builder) {
        super(builder.id);
        this.type = builder.type;
        this.remarks = builder.remarks;
        this.columns = builder.columns;
        this.primaryKey = builder.primaryKey;
        this.uniqueKeyList = builder.uniqueKeyList;
        this.foreignKeyList = builder.foreignKeyList;
        this.checkExpressionList = builder.checkExpressionList;
        this.tableProperties = builder.tableProperties;
    }

    public Database getDb() {
        return getId().getContainerId();
    }

    public String getFullName() {
        return getDb().getFullTableName(getId());
    }

    public String getCatalog() {
        return getId().getCatalog();
    }

    public String getSchema() {
        return getId().getSchema();
    }

    public String getType() {
        return type;
    }

    public String getRemarks() {
        return remarks;
    }

    public int getNumberOfColumns() {
        return columns.size();
    }

    public Column getColumn(int index) {
        return columns.get(index);
    }

    public Column getColumn(String columnName) {
        Column col = columns.get(columnName);
        if (col != null) return col;
        throw new IllegalArgumentException(String.format("Column %s doesn't exist in table %s",
                columnName, getId().getFullName()));
    }

    public int getColumnIndex(String columnName) {
        return getColumn(columnName).getOrdinalPosition();
    }

    public Collection<Column> getColumnCollection() {
        return Collections.unmodifiableCollection(columns.values());
    }

    public Key getPrimaryKey() {
        return primaryKey;
    }

    public Collection<Key> getUniqueKeyCollection() {
        return Collections.unmodifiableCollection(uniqueKeyList);
    }

    public Collection<ForeignKey> getForeignKeyCollection() {
        return Collections.unmodifiableCollection(foreignKeyList);
    }

    public Collection<Check> getCheckExpressionCollecttion() {
        return Collections.unmodifiableCollection(checkExpressionList);
    }

    public String getTableProperties() {
        return tableProperties;
    }

}
