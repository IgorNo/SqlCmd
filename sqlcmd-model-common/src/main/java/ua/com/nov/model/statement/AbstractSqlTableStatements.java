package ua.com.nov.model.statement;

import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.metadata.constraint.Check;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.metadata.column.Column;
import ua.com.nov.model.entity.metadata.table.metadata.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.metadata.constraint.Key;
import ua.com.nov.model.entity.metadata.table.TableId;

public abstract class AbstractSqlTableStatements extends BaseSqlStmtSource<TableId, Table, Database> {
        public static final String CREATE_TABLE_SQL = "CREATE TABLE %s (%s) %s";
        public static final String DROP_TABLE_SQL = "DROP TABLE %s";
        public static final String RENAME_TABLE_SQL = "ALTER TABLE %s RENAME TO %s";

        @Override
        public String getCreateStmt(Table table) {
            return String.format(CREATE_TABLE_SQL,
                    table.getFullName(), getCreateTableDefinition(table), table.getTableProperties());
        }

        @Override
        public String getDeleteStmt(TableId id) {
            return String.format(DROP_TABLE_SQL, id.getName());
        }

        @Override
        public String getUpdateStmt(Table table) {
            return String.format(RENAME_TABLE_SQL, table.getId().getName(), table.getNewName());
        }

        private String getCreateTableDefinition(Table table) {
            int numberOfColumns = table.getNumberOfColumns();
            if (numberOfColumns == 0) return "";

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < numberOfColumns; i++) {
                if (i != 0) result.append(", ");
                addColumnDefinition(i + 1, table, result);
            }

            addKey(table.getPrimaryKey(), ", PRIMARY KEY", result);

            for (Key key : table.getUniqueKeyCollection()) {
                addKey(key, ", UNIQUE", result);
            }

            for (ForeignKey key : table.getForeignKeyCollection()) {
                addForeignKey(key, result);
            }

            for (Check chekExpr : table.getCheckExpressionCollecttion()) {
                result.append(", CHECK(").append(chekExpr).append(')');
            }

            return result.toString();
        }

        private void addColumnDefinition(int ordinalPosition, Table table, StringBuilder result) {
            Column col = table.getColumn(ordinalPosition);
            if (col.getName().trim().isEmpty() || col.getDataType().getTypeName().trim().isEmpty()) {
                throw new IllegalArgumentException();
            }
            result.append(col.getName()).append(' ');
            addFullTypeName(col, result);
            addNotNull(col, result);
            addDefaultValue(col, result);
        }

        protected abstract void addFullTypeName(Column col, StringBuilder result);

        protected void addSizeAndPrecision(Column col, StringBuilder result) {
            if (col.getColumnSize() != null) {
                result.append('(').append(col.getColumnSize());
                if (col.getPrecision() != null) result.append(',').append(col.getPrecision());
                result.append(')');
            }
        }

        private void addNotNull(Column col, StringBuilder result) {
            if (col.getNullable() == 2) throw new IllegalArgumentException();
            if (col.getNullable() == 0) result.append(" NOT NULL");
        }

        private void addDefaultValue(Column col, StringBuilder result) {
            if (col.getDefaultValue() != null && !col.getDefaultValue().trim().isEmpty()) {
                result.append(" DEFAULT ").append(col.getDefaultValue());
            }
        }

        private void addKey(Key key, String keyType, StringBuilder result) {
            int numberOfKeyColumns = key.getNumberOfColumns();
            if (numberOfKeyColumns > 0) {
                result.append(keyType).append(" (");
                for (int i = 1; i <= numberOfKeyColumns; i++) {
                    result.append(key.getColumn(i));
                    if (i != numberOfKeyColumns) result.append(',');
                }
                result.append(')');
            }
        }

        private void addForeignKey(ForeignKey key, StringBuilder result) {
            int numberOfKeyColumns = key.getNumberOfColumns();
            if (numberOfKeyColumns > 0) {
                result.append(", FOREIGN KEY").append(" (");
                for (int i = 1; i <= numberOfKeyColumns; i++) {
                    result.append(key.getFkColumn(i));
                    if (i != numberOfKeyColumns) result.append(',');
                }
                result.append(')');
                result.append(" REFERENCES ").append(key.getPkColumn(0).getId().getContainerId().getName()).append(" (");
                for (int i = 1; i <= numberOfKeyColumns; i++) {
                    result.append(key.getPkColumn(i));
                    if (i != numberOfKeyColumns) result.append(',');
                }
                result.append(')');
            }
            if (key.getDeleteRule() != null) {
                result.append(" ON DELETE ").append(key.getDeleteRule().getAction());
            }
            if (key.getUpdateRule() != null) {
                result.append(" ON UPDATE ").append(key.getUpdateRule().getAction());
            }
        }
    }
