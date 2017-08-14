package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.Buildable;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.Persistance;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.datatype.DbDataType;
import ua.com.nov.model.entity.metadata.schema.Schema;
import ua.com.nov.model.entity.metadata.schema.SchemaMd;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.constraint.*;

import java.util.*;

public class Table extends SchemaMd<Table.Id> implements Persistance<Schema.Id> {

    private final Map<String, Column> columns; // all table addColumnList

    // all table constraint (primary key, foreign keys, unique keys, checks)
    private final Map<Class<? extends Constraint>, Set<? extends Constraint>> constraints;

    private final Set<Index> indices; // table indices list

    protected Table(Builder builder) {
        super(builder.id, builder.type, builder.options);
        setViewName(builder.viewName);
        this.constraints = builder.constraints;
        this.columns = builder.columns;
        this.indices = builder.indices;
        for (Index index : new ArrayList<>(indices)) {
            if (builder.isContainsIndex(index)) indices.remove(index);
        }
    }

    @Override
    public Server getServer() {
        return getId().getServer();
    }

    @Override
    public Schema.Id getContainerId() {
        return getId().getContainerId();
    }

    public String getFullName() {
        return getId().getFullName();
    }

    public String getViewName() {
        return super.getViewName();
    }

    public int getNumberOfColumns() {
        return columns.size();
    }

    public Column getColumn(int index) {
        for (Column column : columns.values()) {
            if (column.getOrdinalPosition() == index)
                return column;
        }
        return null;
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

    public List<Column> getColumns() {
        List<Column> result = new ArrayList<>(columns.values());
        Collections.sort(result, (o1, o2) -> o1.getOrdinalPosition() - o2.getOrdinalPosition());
        return result;
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

    public List<UniqueKey> getUniqueKeyList() {
        List<UniqueKey> result = new LinkedList<>();
        result.addAll((Set<UniqueKey>) constraints.get(UniqueKey.class));
        return result;
    }

    public UniqueKey getUniqueKey(String name) {
        for (UniqueKey key : getUniqueKeyList()) {
            if (key.getName().equalsIgnoreCase(name)) return key;
        }
        return null;
    }

    public List<ForeignKey> getForeignKeyList() {
        List<ForeignKey> result = new LinkedList<>();
        if (constraints.containsKey(ForeignKey.class))
            result.addAll((Set<ForeignKey>) constraints.get(ForeignKey.class));
        return result;
    }

    public ForeignKey getForeignKey(String name) {
        for (ForeignKey key : getForeignKeyList()) {
            if (key.getName().equalsIgnoreCase(name)) return key;
        }
        return null;
    }

    public ForeignKey getForeignKey(Table.Id id) {
        for (ForeignKey key : getForeignKeyList()) {
            if (key.getPkColumn(1).getTableId().equals(id)) return key;
        }
        return null;
    }

    public List<Index> getIndexList() {
        List<Index> result = new LinkedList<>();
        result.addAll(indices);
        return result;
    }

    public Index getIndex(String name) {
        for (Index key : getIndexList()) {
            if (key.getName().equalsIgnoreCase(name)) return key;
        }
        return null;
    }

    public Collection<Check> getCheckExpressionCollecttion() {
        return (Set<Check>) constraints.get(Check.class);
    }

    @Override
    public String getCreateStmtDefinition(String conflictOption) {
        StringBuilder sb = new StringBuilder();
        if (columns.size() != 0) {
            sb.append(" (\n");
            String s = "";
            for (TableMd md : getMetaData()) {
                sb.append(s).append('\t').append(md.getCreateStmtDefinition(null));
                if (s.isEmpty()) s = ",\n";
            }
            sb.append("\n)");
        }
        return String.format(super.getCreateStmtDefinition(conflictOption), sb.toString());
    }

    public static class Builder implements Buildable<Table> {
        private final Id id;     // table object identifier
        private final Map<String, Column> columns = new LinkedHashMap<>(); // all table addColumnList
        private final Map<Class<? extends Constraint>, Set<? extends Constraint>> constraints =
                new LinkedHashMap<>(); // all table constraint (primary key, foreign keys, unique keys, checks, etc)
        private String type;
        private Optional<Table> options;
        private Set<Index> indices = new HashSet<>(); // table indices list
        private int ordinalPosition = 1;

        private String viewName;

        public Builder(Database.Id db, String name, String catalog, String schema) {
            this(new Id(db, name, catalog, schema));
        }

        public Builder(Id id) {
            this.id = id;
            constraints.put(PrimaryKey.class, new LinkedHashSet<PrimaryKey>(1));
        }

        public Builder(Table table) {
            this(table.getId());
            this.addColumnList(table.getColumns());
            this.addConstraintList(table.getAllConstrains());
            this.addIndexList(table.getIndexList());
            this.options((Optional<Table>) table.getOptions());
            this.type(table.getType());
            this.viewName(table.getViewName());
        }

        public Id getId() {
            return id;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder viewName(String viewName) {
            this.viewName = viewName;
            return this;
        }

        public Builder addColumnList(Collection<Column> columns) {
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
            columnBuilder.ordinalPosition(ordinalPosition++);
            if (columnBuilder.isPrimaryKey() || columnBuilder.isUnique()) columnBuilder.nullable(DbDataType.NOT_NULL);
            addColumn(columnBuilder.build());
            for (Constraint.Builder<? extends Constraint> constraintBuilder : columnBuilder.getConstraints()) {
                addConstraint(constraintBuilder);
            }
            return this;
        }

        private void checkColumnsInKey(Collection<String> columnNames) {
            for (String s : columnNames) {
                if (columns.get(s.toLowerCase()) == null)
                    throw new IllegalArgumentException(String.format("Column with name '%s' doesn't exists in table '%s'",
                            s, getId().getFullName()));
            }
        }

        private void checkConstraint(Constraint addingConstraint) {
            for (Set<? extends Constraint> set : constraints.values()) {
                for (Constraint constraint : set) {
                    if (constraint.getName().equalsIgnoreCase(addingConstraint.getName()))
                        throw new IllegalArgumentException(String.format("Constraint with name '%s' already exists",
                                addingConstraint.getName()));
                }
            }
        }

        public <V extends Constraint> Builder addConstraint(V.Builder<V> builder) {
            builder.setTableId(getId());
            addConstraint(builder.build());
            return this;
        }

        public <V extends Constraint> Builder addConstraint(V constraint) {
            checkTableId(constraint);
            if (constraint instanceof Key) {
                checkColumnsInKey(((Key) constraint).getColumnNamesList());
            }

            Set<V> constraintSet = (Set<V>) constraints.get(constraint.getClass());
            if (constraintSet == null) {
                constraintSet = new LinkedHashSet<>();
                constraints.put(constraint.getClass(), constraintSet);
            } else {
                if (constraint.getClass() == PrimaryKey.class) {
                    if (constraintSet.isEmpty()) {
                        Set<UniqueKey> uniqueKeys = (Set<UniqueKey>) constraints.get(UniqueKey.class);
                        if (uniqueKeys != null && uniqueKeys.contains(constraint))
                            uniqueKeys.remove(constraint);
                    } else {
                        throw new IllegalArgumentException("The table must contain only one primary key");
                    }
                }
                checkConstraint(constraint);
            }

            if (!constraintSet.add(constraint))
                throw new IllegalArgumentException(String.format("Table %s already contains '%s'",
                        id.getFullName(), constraint));

            return this;
        }

        public <V extends Constraint> Builder addConstraintList(List<V> constraintList) {
            for (V check : constraintList) {
                addConstraint(check);
            }
            return this;
        }

        public Builder addIndex(Index.Builder builder) {
            builder.setTableId(getId());
            addIndex(builder.build());
            return this;
        }

        private Builder addIndex(Index index) {
            checkTableId(index);
            if (!indices.add(index)) {
                throw new IllegalArgumentException(String.format("Index %s already exists in table %s",
                        index.getColumnNames(), index.getTableId().getFullName()));
            }
            return this;
        }

        public Builder addIndexList(List<Index> indexList) {
            for (Index index : indexList) {
                addIndex(index);
            }
            return this;
        }

        private boolean isContainsIndex(Index index) {
            for (Set<? extends Constraint> set : constraints.values()) {
                if (set.contains(index)) return true;
            }
            return false;
        }

        public Builder options(Optional<Table> options) {
            this.options = options;
            return this;
        }

        public Table build() {
            return new Table(this);
        }
    }

    public static class Id extends SchemaMd.Id {

        public Id(Schema.Id schemaId, String name) {
            super(schemaId, name);
        }

        public Id(Database.Id db, String name, String catalog, String schema) {
            super(new Schema.Id(db, catalog, schema), name);
        }

        @Override
        public String getMdName() {
            return "TABLE";
        }
    }
}
