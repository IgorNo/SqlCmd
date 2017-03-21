package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.Building;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.AbstractMetaData;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.constraint.*;

import java.util.*;

public class Table extends AbstractMetaData<TableId> {
    private final String type;    // table type.  Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
                            //                                "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
    private final String remarks;  // explanatory comment on the table

    private final Map<String, Column> columns; // all table columns

    // all table constraint (primary key, foreign keys, unique keys, checks)
    private final Map<Class<? extends Constraint>, Set<? extends Constraint>> constraints;

    private final Set<Index> indices; // table indices list

    private String tableProperties = "";

    public static class Builder implements Building, Unique<TableId> {
        private final TableId id;     // table object identifier
        private final String type;    // table type.  Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
        //                                "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
        private String remarks;  // explanatory comment on the table

        private final Map<String, Column> columns = new LinkedHashMap<>(); // all table columns
        private final Map<Class<? extends Constraint>, Set<? extends Constraint>> constraints =
                new LinkedHashMap<>(); // all table constraint (primary key, foreign keys, unique keys, checks)

        private Set<Index> indices; // table indices list

        private String tableProperties = "";

        public Builder(TableId id) {
            this(id, "TABLE");
        }

        public Builder(Database.DbId db, String name, String catalog, String schema) {
            this(new TableId(db, name, catalog, schema));
        }

        public Builder(TableId id, String type) {
            this.id = id;
            this.type = type;
            constraints.put(PrimaryKey.class, new LinkedHashSet<PrimaryKey>(1));
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
            checkTableId(col);
            if (columns.put(col.getName().toLowerCase(), col) != null) {
                throw new IllegalArgumentException(String.format("Column with name '%s' already exists", col.getName()));
            }
            return this;
        }

        private void checkTableId(TableMd md) {
            if (!md.getId().getContainerId().equals(this.getId())) {
                throw new IllegalArgumentException(String.format("Object '%s' doesn't belong table '%s'.",
                        md.getId().getFullName(), id.getFullName()));
            }
        }

        public Builder addColumn(Column.Builder columnBuilder) {
            columnBuilder.setTableId(getId());
            addColumn(columnBuilder.build());
            return this;
        }

        public Builder primaryKey(PrimaryKey key) {
            checkKey(key);
            Set<PrimaryKey> primaryKey = (Set<PrimaryKey>) constraints.get(PrimaryKey.class);
            if (!primaryKey.isEmpty()) {
                throw new IllegalArgumentException("The table must contain only one primary key");
            }
            primaryKey.add(key);
            return this;
        }

        private void checkKey(Key key) {
            checkConstraint(key);
            checkColumnsInKey(key.getColumnNames());
        }

        private void checkConstraint(Constraint addingConstraint) {
            checkTableId(addingConstraint);
            for (Set<? extends Constraint> set : constraints.values()) {
                for (Constraint constraint : set) {
                    if (constraint.getName().equalsIgnoreCase(addingConstraint.getName()))
                        throw new IllegalArgumentException(String.format("Constraint with name '%s' already exists",
                                addingConstraint.getName()));
                }
            }
        }

        private void checkColumnsInKey(Collection<String> columnNames) {
            for (String s : columnNames) {
                if (columns.get(s.toLowerCase()) == null)
                    throw new IllegalArgumentException(String.format("Column with name '%s' doesn't exists in table '%s'",
                            s, getId().getFullName()));
            }
        }

        public Builder primaryKey(PrimaryKey.Builder builder) {
            builder.setTableId(getId());
            builder.setName("_pkey");
            primaryKey(builder.build());
            return this;
        }

        public Builder addUniqueKey(UniqueKey key) {
            checkKey(key);
            Set<UniqueKey> uniqueKeys = (Set<UniqueKey>) constraints.get(UniqueKey.class);
            if (uniqueKeys == null) {
                uniqueKeys = new LinkedHashSet<>();
                constraints.put(UniqueKey.class, uniqueKeys);
            }
            uniqueKeys.add(key);
            return this;
        }

        public Builder addUniqueKey(UniqueKey.Builder builder) {
            builder.setTableId(getId());
            builder.setName("_unique");
            addUniqueKey(builder.build());
            return this;
        }

        public Builder foreignKeys(List<ForeignKey> foreignKeyList) {
            for (ForeignKey foreignKey : foreignKeyList) {
                addForeignKey(foreignKey);
            }
            return this;
        }

        public Builder addForeignKey(ForeignKey key) {
            checkKey(key);
            Set<ForeignKey> foreignKeys = (Set<ForeignKey>) constraints.get(ForeignKey.class);
            if (foreignKeys == null) {
                foreignKeys = new LinkedHashSet<>();
                constraints.put(ForeignKey.class, foreignKeys);
            }
            foreignKeys.add(key);
            return this;
        }

        public Builder addForeignKey(ForeignKey.Builder builder) {
            builder.setTableId(getId());
            builder.setName("_fkey");
            addForeignKey(builder.build());
            return this;
        }

        public Builder checkExpressionList(List<Check> checkExpressionList) {
            for (Check check : checkExpressionList) {
                addCheckExpression(check);
            }
            return this;
        }

        public Builder addCheckExpression(Check check) {
            checkConstraint(check);
            Set<Check> checkExpressions = (Set<Check>) constraints.get(Check.class);
            if (checkExpressions == null) {
                checkExpressions = new LinkedHashSet<>();
                constraints.put(Check.class, checkExpressions);
            }
            checkExpressions.add(check);
            return this;
        }

        public Builder addCheckExpression(Check.Builder builder) {
            builder.setTableId(getId());
            addCheckExpression(builder.build());
            return this;
        }

        public Builder indexList(List<Index> indexList) {
            for (Index index : indexList) {
                addIndex(index);
            }
            return this;
        }

        private Builder addIndex(Index index) {
            checkTableId(index);
            checkColumnsInKey(index.getColumnNames());
            if (!indices.add(index)) {
                throw new IllegalArgumentException(String.format("Constraint with name '%s' already exists",
                        index.getName()));
            }
            return this;
        }

        public Builder addIndex(Index.Builder builder) {
            builder.setTableId(getId());
            builder.setName("_idx");
            addIndex(builder.build());
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
        this.constraints = builder.constraints;
        this.columns = builder.columns;
        this.type = builder.type;
        this.remarks = builder.remarks;
        this.indices = builder.indices;
        this.tableProperties = builder.tableProperties;
    }

    private void checkTableMd(TableMd md) {
        if (!md.getId().getContainerId().equals(this.getId())) {
            throw new IllegalArgumentException(String.format("Object '%s' doesn't belong table '%s'.",
                    md.getId().getFullName(), getFullName()));
        }
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
        Column col = columns.get(columnName.toLowerCase());
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
        for (PrimaryKey pk : (Set<PrimaryKey>) constraints.get(PrimaryKey.class)) {
            return pk;
        }
        return null;
    }

    public List<Constraint> getAllConstrains() {
        List<Constraint> result = new LinkedList<>();
        for (Collection<? extends Constraint> set : constraints.values()) {
            result.addAll(set);
        }
        return result;
    }

    public List<TableMd> getMetaData() {
        List<TableMd> result = new LinkedList<>();
        result.addAll(getColumns());
        result.addAll(getAllConstrains());
        return result;
    }

    public Collection<UniqueKey> getUniqueKeyCollection() {
        return (Set<UniqueKey>) constraints.get(UniqueKey.class);
    }

    public List<ForeignKey> getForeignKeyList() {
        List<ForeignKey> result = new LinkedList<>();
        result.addAll((Set<ForeignKey>) constraints.get(ForeignKey.class));
        return result;
    }

    public ForeignKey getForeignKey(String name) {
        for (ForeignKey key : getForeignKeyList()) {
            if (key.getName().equalsIgnoreCase(name)) return key;
        }
        throw new IllegalArgumentException(String.format("Foreign key with name '%s' doesn't exist in table '%s'.",
                name, getFullName()));
    }

    public Collection<Check> getCheckExpressionCollecttion() {
        return (Set<Check>) constraints.get(Check.class);
    }

    public String getTableProperties() {
        return tableProperties;
    }

}
