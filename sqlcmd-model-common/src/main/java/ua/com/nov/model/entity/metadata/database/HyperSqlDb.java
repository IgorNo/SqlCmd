package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.dao.statement.AbstractDatabaseMdSqlStatements;
import ua.com.nov.model.dao.statement.AbstractTableMdSqlStatements;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.datatype.DataType;
import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.column.Column;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class HyperSqlDb extends Database {

    public HyperSqlDb(String dbUrl, String dbName, MetaDataOptions<HyperSqlDb> options) {
        super(dbUrl, dbName, null);
        getTypesMap().put(JdbcDataTypes.LONGVARCHAR, "LONGVARCHAR");
        List<DataType> dataTypeList = new ArrayList<>();
        dataTypeList.add(new DataType.Builder("LONGVARCHAR", Types.LONGVARCHAR).build());
        addDataTypes(dataTypeList);
    }

    public HyperSqlDb(String dbUrl, String dbName) {
        this(dbUrl, dbName, null);
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
        return new AbstractDatabaseMdSqlStatements<Id, HyperSqlDb, Database>() {

            @Override
            public SqlStatement getCreateStmt(HyperSqlDb db) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SqlStatement getUpdateStmt(HyperSqlDb db) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SqlStatement getDeleteStmt(HyperSqlDb db) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SqlStatement getRenameStmt(HyperSqlDb db, String newName) {
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
        };
    }
}
