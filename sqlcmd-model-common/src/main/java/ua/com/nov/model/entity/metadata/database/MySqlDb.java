package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.entity.MdCreateOptions;
import ua.com.nov.model.entity.MdUpdateOptions;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.constraint.Constraint;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.Key;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.statement.AbstractColumnSqlStatements;
import ua.com.nov.model.statement.AbstractConstraintSqlStatements;
import ua.com.nov.model.statement.AbstractMetaDataSqlStatements;
import ua.com.nov.model.statement.SqlStatement;

public final class MySqlDb extends Database {

    public MySqlDb(String dbUrl, String dbName) {
        this(dbUrl, dbName, null);
    }

    public MySqlDb(String dbUrl, String dbName, Options dbProperties) {
        super(dbUrl, dbName, dbProperties);
        initTypesMap();
    }

    private void initTypesMap() {
        getTypesMap().put(JdbcDataTypes.INTEGER, "INT");
        getTypesMap().put(JdbcDataTypes.LONGVARCHAR, "TEXT");
        getTypesMap().put(JdbcDataTypes.NUMERIC, "DECIMAL");
    }

    @Override
    public String getAutoIncrementDefinition() {
        return " AUTO_INCREMENT";
    }

//    @Override
//    public String getFullTableName(Table.Id id) {
//        StringBuilder result = new StringBuilder();
//        if (id.getCatalog() != null) result.append(id.getCatalog()).append('.');
//        return result.append(id.getName()).toString();
//    }

    @Override
    public String convert(String parameter) {
        if (parameter != null) return parameter.toLowerCase();
        return parameter;
    }

    @Override
    public AbstractMetaDataSqlStatements getDatabaseSqlStmtSource() {
        return new AbstractMetaDataSqlStatements<Database.Id, MySqlDb, MySqlDb>() {
            @Override
            public SqlStatement getReadAllStmt(MySqlDb containerId) {
                return new SqlStatement.Builder("SHOW DATABASES").build();
            }
        };
    }

    @Override
    public AbstractColumnSqlStatements getColumnSqlStmtSource() {
        return new AbstractColumnSqlStatements() {
            @Override
            public SqlStatement getUpdateStmt(Column col) {
                return null;
//                return String.format("ALTER TABLE %s CHANGE COLUMN %s %s %s",
//                        col.getTableId().getFullName(), col.getName(), col.getNewName(), col.getFullTypeDeclaration());
            }
        };
    }

    @Override
    public AbstractConstraintSqlStatements getPrimaryKeySqlStmtSource() {
        return new AbstractConstraintSqlStatements<PrimaryKey.Id, PrimaryKey>() {
            @Override
            public SqlStatement getDeleteStmt(PrimaryKey.Id id) {
                return new SqlStatement.Builder("ALTER TABLE %s DROP PRIMARY KEY",
                        id.getTableId().getFullName()).build();
            }
        };
    }

    @Override
    public AbstractConstraintSqlStatements getForeignKeySqlStmtSource() {
        return new AbstractConstraintSqlStatements<ForeignKey.Id, ForeignKey>() {
            @Override
            public SqlStatement getDeleteStmt(ForeignKey.Id id) {
                return new SqlStatement.Builder("ALTER TABLE %s DROP FOREIGN KEY %s",
                        id.getTableId().getFullName(), id.getName()).build();
            }
        };
    }

    private abstract static class KeySqlStatements<K extends Constraint.Id, V extends Key> extends AbstractConstraintSqlStatements<K,V> {
        @Override
        public SqlStatement getUpdateStmt(V pk) {
            return new SqlStatement.Builder("ALTER TABLE %s RENAME INDEX %s TO %s",
                    pk.getTableId().getFullName(), pk.getName(), pk.getNewName()).build();
        }

        @Override
        public SqlStatement getDeleteStmt(K id) {
            return new SqlStatement.Builder("ALTER TABLE %s DROP INDEX %s",
                    id.getTableId().getFullName(), id.getName()).build();
        }

    }

    @Override
    public AbstractMetaDataSqlStatements getIndexSqlStmtSource() {
        return new AbstractMetaDataSqlStatements<Index.Id, Index, Table.Id>() {
            @Override
            public SqlStatement getDeleteStmt(Index.Id id) {
                return new SqlStatement.Builder(super.getDeleteStmt(id).getSql()
                        + " ON " + id.getTableId().getFullName()).build();
            }
        };
    }

    public static class Options extends MetaDataOptions implements MdCreateOptions, MdUpdateOptions {
        private String characterSet;
        private String collate;

        public Options(String charSet, String collate) {
            this.characterSet = charSet;
            this.collate = collate;
            optionList.add(toString());
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            if (characterSet != null)
                sb.append("CHARACTER SET = ").append(characterSet);
            if (collate != null)
                sb.append(" COLLATE  = ").append(collate);
            return sb.toString();
        }
    }
}

