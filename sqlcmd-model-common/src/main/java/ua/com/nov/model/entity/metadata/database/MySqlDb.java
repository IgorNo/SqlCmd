package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.dao.statement.AbstractColumnSqlStatements;
import ua.com.nov.model.dao.statement.AbstractConstraintSqlStatements;
import ua.com.nov.model.dao.statement.AbstractMetaDataSqlStatements;
import ua.com.nov.model.dao.statement.SqlStatement;
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

    public abstract static class Options extends MetaDataOptions {
        public abstract static class Builder extends MetaDataOptions.Builder<Options> {
            protected String characterSet;
            protected String collate;

            public Builder() {
            }

            public Builder characterSet(String characterSet) {
                this.characterSet = characterSet;
                return this;
            }

            public Builder collate(String collate) {
                this.collate = collate;
                return this;
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

        public Options(Builder builder) {
            super(builder);
            optionList.add(builder.toString());
        }

        @Override
        public String toString() {
            return optionList.get(0);
        }
    }

    public static class CreateOptions extends Options implements MdCreateOptions {
        public static class Builder extends Options.Builder {
            public Builder() {
            }

            public Builder existOptions(boolean existOptions) {
                if (existOptions)
                    setExistOptions("IF NOT EXISTS");
                return this;
            }

            @Override
            public CreateOptions build() {
                return new CreateOptions(this);
            }
        }

        public CreateOptions(Builder builder) {
            super(builder);
        }
    }

    public static class UpdateOptions extends Options implements MdUpdateOptions {
        public static class Builder extends Options.Builder {
            @Override
            public Options build() {
                return new UpdateOptions(this);
            }
        }
        public UpdateOptions(Builder builder) {
            super(builder);
        }
    }
}

