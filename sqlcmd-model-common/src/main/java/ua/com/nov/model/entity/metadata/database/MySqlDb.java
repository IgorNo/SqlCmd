package ua.com.nov.model.entity.metadata.database;

import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.dao.statement.AbstractColumnSqlStatements;
import ua.com.nov.model.dao.statement.AbstractConstraintSqlStatements;
import ua.com.nov.model.dao.statement.AbstractMetaDataSqlStatements;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.schema.Schema;
import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.constraint.Constraint;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class MySqlDb extends Database {

    public MySqlDb(String dbUrl, String dbName, MetaDataOptions<MySqlDb> options) {
        super(dbUrl, dbName, options);
        initTypesMap();
    }

    public MySqlDb(String dbUrl, String dbName) {
        this(dbUrl, dbName, null);
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

    @Override
    public String convert(String parameter) {
        if (parameter != null) return parameter.toLowerCase();
        return parameter;
    }

    @Override
    public AbstractMetaDataSqlStatements getDatabaseSqlStmtSource() {
        return new AbstractMetaDataSqlStatements<Database.Id, MySqlDb, Database>() {
            @Override
            public SqlStatement getReadAllStmt(Database cId) {
                return new SqlStatement.Builder("SHOW DATABASES").build();
            }

            @Override
            public SqlStatement getReadOptionsStmt(Id eId) {
                return new SqlStatement.Builder(
                        "SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME  " +
                                "FROM information_schema.SCHEMATA " +
                                "WHERE SCHEMA_NAME = '" + eId.getName() + "'").build();
            }

            @Override
            public RowMapper<Optional<MySqlDb>> getOptionsRowMapper() {
                return new RowMapper<Optional<MySqlDb>>() {
                    @Override
                    public Optional<MySqlDb> mapRow(ResultSet rs, int i) throws SQLException {
                        return new MySqlDb.Options.Builder()
                                .characterSet(rs.getString(1)).collate(rs.getString(2))
                                .build();
                    }
                };
            }

            @Override
            public SqlStatement getRenameStmt(Id eId, String newName) {
                throw new UnsupportedOperationException();
            }

        };
    }

    @Override
    public AbstractMetaDataSqlStatements<Table.Id, Table, Schema.Id> getTableSqlStmtSource() {
        return new AbstractMetaDataSqlStatements<Table.Id, Table, Schema.Id>() {
            @Override
            public String getCommentStmt(Table table) {
                if (table.getViewName() == null) return "";
                return  String.format("ALTER TABLE %s COMMENT '%s'", table.getFullName(), table.getViewName());
            }

            @Override
            public SqlStatement getReadOptionsStmt(Table.Id eId) {
                return new SqlStatement.Builder(
                        "SELECT ENGINE, ROW_FORMAT, AVG_ROW_LENGTH, AUTO_INCREMENT, " +
                                "TABLE_COLLATION, CHECKSUM, TABLE_COMMENT " +
                                "FROM information_schema.TABLES " +
                                "WHERE TABLE_NAME = '" + eId.getName() + "'").build();
            }

            @Override
            public RowMapper<Optional<Table>> getOptionsRowMapper() {
                return new RowMapper<Optional<Table>>() {
                    @Override
                    public Optional<Table> mapRow(ResultSet rs, int i) throws SQLException {
                        return new MySqlTableOptions.Builder()
                                .engine(rs.getString(1)).avgRowLength(rs.getInt(2))
                                .autoIncrement(rs.getInt(3)).defaultCharset(rs.getString(4))
                                .checkSum(rs.getBoolean(5)).comment(rs.getString(6))
                                .build();
                    }
                };
            }
        };
    }

    @Override
    public AbstractColumnSqlStatements getColumnSqlStmtSource() {
        return new AbstractColumnSqlStatements() {
//            @Override
//            public SqlStatement getUpdateStmt(Column col) {
//                return null;
////                return String.format("ALTER TABLE %s CHANGE COLUMN %s %s %s",
////                        col.getTableId().getFullName(), col.getName(), col.getNewName(), col.getFullTypeDeclaration());
//            }
        };
    }

    @Override
    public AbstractConstraintSqlStatements getPrimaryKeySqlStmtSource() {
        return new AbstractConstraintSqlStatements<PrimaryKey.Id, PrimaryKey>() {
////            @Override
////            public SqlStatement getDeleteStmt(PrimaryKey.Id eId) {
////                return new SqlStatement.Builder("ALTER TABLE %s DROP PRIMARY KEY",
////                        eId.getTableId().getFullName()).build();
////            }
        };
    }

    @Override
    public AbstractConstraintSqlStatements getForeignKeySqlStmtSource() {
        return new AbstractConstraintSqlStatements<ForeignKey.Id, ForeignKey>() {
//            @Override
//            public SqlStatement getDeleteStmt(ForeignKey.Id eId) {
//                return new SqlStatement.Builder("ALTER TABLE %s DROP FOREIGN KEY %s",
//                        eId.getTableId().getFullName(), eId.getName()).build();
//            }
        };
    }

    private abstract static class KeySqlStatements<K extends Constraint.Id, V extends Constraint<K>> extends AbstractConstraintSqlStatements<K,V> {
//        @Override
//        public SqlStatement getUpdateStmt(V pk) {
//            return new SqlStatement.Builder("ALTER TABLE %s RENAME INDEX %s TO %s",
//                    pk.getTableId().getFullName(), pk.getName(), pk.getNewName()).build();
//        }
//
//        @Override
//        public SqlStatement getDeleteStmt(K eId) {
//            return new SqlStatement.Builder("ALTER TABLE %s DROP INDEX %s",
//                    eId.getTableId().getFullName(), eId.getName()).build();
//        }

    }

    @Override
    public AbstractMetaDataSqlStatements getIndexSqlStmtSource() {
        return new AbstractMetaDataSqlStatements<Index.Id, Index, Table.Id>() {
//            @Override
//            public SqlStatement getDeleteStmt(Index.Id eId) {
//                return new SqlStatement.Builder(super.getDeleteStmt(eId).getSql()
//                        + " ON " + eId.getTableId().getFullName()).build();
//            }
        };
    }

    public static class Options extends MetaDataOptions<MySqlDb> {
        public static class Builder extends MetaDataOptions.Builder<Options> {
            public Builder() {
                super(MySqlDb.class);
            }

            public Builder characterSet(String characterSet) {
                addOption("CHARACTER SET", characterSet);
                return this;
            }

            public Builder collate(String collate) {
                addOption("COLLATE", collate);
                return this;
            }

            @Override
            public Options build() {
                return new Options(this);
            }
        }

        public Options(Builder builder) {
            super(builder);
        }

        public String getCharacterSet() {
            return getOption("CHARACTER SET");
        }

        public String getCollate() {
            return getOption("COLLATE");
        }

    }
}

