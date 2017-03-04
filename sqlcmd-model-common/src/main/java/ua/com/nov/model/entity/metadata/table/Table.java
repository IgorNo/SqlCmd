package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.Building;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.AbstractMetaData;
import ua.com.nov.model.entity.metadata.table.constraint.*;
import ua.com.nov.model.entity.metadata.database.Database;

import java.util.*;

public class Table extends AbstractMetaData<TableId> {
    private final String type;    // table type.  Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
                            //                                "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
    private final String remarks;  // explanatory comment on the table

    private final Map<String, Column> columns; // all table columns
    private final PrimaryKey primaryKey; // table primary key
    private final List<UniqueKey> uniqueKeys; // table unique keys list
    private final List<ForeignKey> foreignKeys; // table foreign keys list
    private final List<Check> checkExpressions; // table check expressions list
    private final List<Index> indices; // table indices list

    private String tableProperties = "";

    public static class Builder implements Building, Unique<TableId> {
        private final TableId id;     // table object identifier
        private final String type;    // table type.  Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
        //                                "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
        private String remarks;  // explanatory comment on the table

        private Map<String, Column> columns = new HashMap<>(); // all table columns
        private PrimaryKey primaryKey; // table primary key column
        private List<UniqueKey> uniqueKeys = new LinkedList<>(); // table unique keys list
        private List<ForeignKey> foreignKeys = new LinkedList<>(); // table foreign keys list
        private List<Check> checkExpressions = new LinkedList<>(); // table check expressions list
        private List<Index> indices; // table indices list

        private String tableProperties = "";

        private int ordinalPosition = 1;

        public Builder(TableId id) {
            this(id, "TABLE");
        }

        public Builder(Database.DbId db, String name, String catalog, String schema) {
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

        public Builder columns(Collection<Column> columns) {
            for (Column col : columns) {
                addColumn(col);
            }
            return this;
        }

        public Builder addColumn(Column col) {
            if (!col.getId().getContainerId().equals(this.getId())) {
                throw new IllegalArgumentException(String.format("Column '%s' doesn't belong table '%s'.",
                        col.getId().getFullName(), id.getFullName()));
            }
            if (columns.put(col.getName(), col) != null) {
                throw new IllegalArgumentException(String.format("Column with name '%s' alredy exists",
                        col.getName()));
            }
            col.ordinalPosition = this.ordinalPosition++;
            return this;
        }

        public Builder primaryKey(PrimaryKey primaryKey) {
            this.primaryKey = primaryKey;
            return this;
        }

        public Builder uniqueKeyList(List<UniqueKey> uniqueKeyList) {
            this.uniqueKeys = uniqueKeyList;
            return this;
        }

        public Builder addUniqueKey(UniqueKey uniqueKey) {
            this.uniqueKeys.add(uniqueKey);
            return this;
        }

        public Builder foreignKeys(List<ForeignKey> foreignKeyList) {
            this.foreignKeys = foreignKeyList;
            return this;
        }

        public Builder addForeignKey(ForeignKey foreignKey) {
            this.foreignKeys.add(foreignKey);
            return this;
        }

        public Builder checkExpressionList(List<Check> checkExpressionList) {
            this.checkExpressions = checkExpressionList;
            return this;
        }

        public Builder addCheckExpression(Check checkExpression) {
            checkExpressions.add(checkExpression);
            return this;
        }

        public Builder indexList(List<Index> indexList) {
            this.indices = indexList;
            return this;
        }

        public Builder addIndex(Index index) {
            indices.add(index);
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
        this.uniqueKeys = builder.uniqueKeys;
        this.foreignKeys = builder.foreignKeys;
        this.checkExpressions = builder.checkExpressions;
        this.indices = builder.indices;
        this.tableProperties = builder.tableProperties;
    }

    public Database getDb() {
        return getId().getDb();
    }

    public String getFullName() {
        return getId().getFullName();
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

    public Collection<Column> getColumns() {
        List<Column> result = new ArrayList<Column>(columns.values());
        Collections.sort(result, new Comparator<Column>() {
            @Override
            public int compare(Column o1, Column o2) {
                return o1.getOrdinalPosition() - o2.getOrdinalPosition();
            }
        });
        return Collections.unmodifiableCollection(result);
    }

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public Collection<Constraint> getConstrains() {
        List<Constraint> result = new LinkedList<>();
        result.add(primaryKey);
        result.addAll(foreignKeys);
        result.addAll(uniqueKeys);
        return Collections.unmodifiableCollection(result);
    }

    public Collection<TableMd> getMetaData() {
        List<TableMd> result = new LinkedList<>();
        result.addAll(getColumns());
        result.addAll(getConstrains());
        return Collections.unmodifiableCollection(result);
    }

    public Collection<UniqueKey> getUniqueKeyCollection() {
        return Collections.unmodifiableCollection(uniqueKeys);
    }

    public Collection<ForeignKey> getForeignKeyCollection() {
        return Collections.unmodifiableCollection(foreignKeys);
    }

    public Collection<Check> getCheckExpressionCollecttion() {
        return Collections.unmodifiableCollection(checkExpressions);
    }

    public String getTableProperties() {
        return tableProperties;
    }

}
