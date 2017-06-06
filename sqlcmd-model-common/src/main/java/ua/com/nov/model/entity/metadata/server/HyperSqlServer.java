package ua.com.nov.model.entity.metadata.server;

import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.dao.statement.AbstractDatabaseMdSqlStatements;
import ua.com.nov.model.dao.statement.AbstractTableMdSqlStatements;
import ua.com.nov.model.dao.statement.OptionsSqlStmtSource;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.datatype.DataType;
import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.grantee.User;
import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.column.ColumnOptions;
import ua.com.nov.model.entity.metadata.table.column.HyperSqlColumnOptions;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class HyperSqlServer extends Server {

    public HyperSqlServer(String dbUrl) {
        super(dbUrl);
        getTypesMap().put(JdbcDataTypes.LONGVARCHAR, "LONGVARCHAR");
        List<DataType> dataTypeList = new ArrayList<>();
        dataTypeList.add(new DataType.Builder("LONGVARCHAR", Types.LONGVARCHAR).build());
        addDataTypes(dataTypeList);
    }

    @Override
    public ColumnOptions.Builder<HyperSqlColumnOptions> createColumnOptions() {
        return new HyperSqlColumnOptions.Builder();
    }

    @Override
    public String convert(String parameter) {
        if (parameter != null) return parameter.toUpperCase();
        return parameter;
    }

    @Override
    public AbstractDatabaseMdSqlStatements getDatabaseSqlStmtSource() {
        return new AbstractDatabaseMdSqlStatements<Database.Id, Database, Server.Id>() {

            @Override
            public SqlStatement getCreateStmt(Database db) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SqlStatement getUpdateStmt(Database db) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SqlStatement getDeleteStmt(Database db) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SqlStatement getRenameStmt(Database db, String newName) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public <I extends TableMd.Id, E extends TableMd> AbstractTableMdSqlStatements<I, E> getTableMdSqlStmtSource() {
        return new AbstractTableMdSqlStatements<I, E>() {
            @Override
            public SqlStatement getRenameStmt(E entity, String newName) {
                return new SqlStatement.Builder(String.format("ALTER %s %s RENAME TO %s",
                       entity.getId().getMdName(), entity.getName(), newName)).build();
            }
        };
    }

    @Override
    public AbstractTableMdSqlStatements<Column.Id, Column> getColumnSqlStmtSource() {
        return new AbstractTableMdSqlStatements<Column.Id, Column>() {
            @Override
            public SqlStatement getRenameStmt(Column col, String newName) {
                return new SqlStatement.Builder(String.format("ALTER TABLE %s ALTER COLUMN %s RENAME TO %s",
                        col.getTableId().getFullName(), col.getName(), newName)).build();
            }

            @Override
            public SqlStatement getUpdateStmt(Column col) {
                String alterSql = String.format("ALTER TABLE %s ALTER COLUMN %s",
                        col.getTableId().getFullName(), col.getName());
                String sql = "";
                if (col.getDataType() != null) {
                    sql = alterSql + " SET DATA TYPE " + col.getFullTypeDeclaration() + ";\n";
                }
                for (String set : col.getOptions().getUpdateOptionsDefinition()) {
                    sql += alterSql + " " + set + ";\n";
                }
                sql += getCommentStmt(col);
                return new SqlStatement.Builder(sql).build();
            }
        };
    }

    @Override
    public OptionsSqlStmtSource<User.Id, User> getUserOptionsSqlStmSource() {
        return new OptionsSqlStmtSource<User.Id, User>() {
            @Override
            public SqlStatement getReadAllOptionsStmt() {
                return null;
            }

            @Override
            public SqlStatement getReadOptionsStmt(User.Id eId) {
                return null;
            }

            @Override
            public RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<User>>> getOptionsRowMapper() {
                return null;
            }
        };
    }
}