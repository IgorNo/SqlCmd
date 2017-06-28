package ua.com.nov.model.dao.statement;

import org.springframework.jdbc.core.SqlParameterValue;
import ua.com.nov.model.dao.fetch.FetchParametersSource;
import ua.com.nov.model.entity.data.Row;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;

import java.sql.Types;
import java.util.List;

public abstract class AbstractDataStmtSource implements DataManipulationSqlStmtSource<Row.Id, Row, Table> {

    private static String whereIdExpression(List<String> idColumns) {
        StringBuilder sb = new StringBuilder();
        String s = "";
        for (String column : idColumns) {
            sb.append(s).append(column).append(" = ").append('?');
            s = " AND ";
        }
        return sb.toString();
    }

    @Override
    public SqlStatement getCreateStmt(Row row) {
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(row.getTable().getFullName());
        List<Column> columns = row.getTable().getColumns();

        String s = " (";
        for (Column column : columns) {
            if (!column.isAutoIncrement()) {
                sql.append(s).append(column.getName());
                s = ", ";
            }
        }
        sql.append(") VALUES ");

        s = "(";
        for (Column column : columns) {
            if (!column.isAutoIncrement()) {
                sql.append(s).append('?');
                s = ", ";
            }
        }
        sql.append(')');

        SqlStatement.Builder builder = new SqlStatement.Builder(sql.toString());
        for (int i = 0; i < columns.size(); i++) {
            if (!columns.get(i).isAutoIncrement()) {
                builder.addParameter(new SqlParameterValue(row.getValueSqlType(i), row.getValue(i)));
            }
        }
        return builder.build();
    }

    @Override
    public SqlStatement getUpdateStmt(Row row) {
        StringBuilder sql = new StringBuilder("UPDATE ").append(row.getTable().getFullName());

        List<Column> columns = row.getTable().getColumns();
        List<String> idColumns = row.getTable().getPrimaryKey().getColumnNamesList();
        String s = " SET ";
        for (Column column : columns) {
            if (!idColumns.contains(column.getName())) {
                sql.append(s).append(column.getName()).append(" = ?");
                s = ", ";
            }
        }

        sql.append(" WHERE ").append(whereIdExpression(idColumns));

        SqlStatement.Builder builder = new SqlStatement.Builder(sql.toString());
        for (int i = 0; i < columns.size(); i++) {
            if (!idColumns.contains(columns.get(i).getName())) {
                builder.addParameter(new SqlParameterValue(row.getValueSqlType(i), row.getValue(i)));
            }
        }
        for (int i = 0; i < idColumns.size(); i++) {
            Row.Id id = row.getId();
            builder.addParameter(new SqlParameterValue(id.getValueSqlType(i), id.getValue(i)));
        }

        return builder.build();
    }

    @Override
    public SqlStatement getDeleteStmt(Row row) {
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(row.getTable().getFullName());

        List<String> idColumns = row.getTable().getPrimaryKey().getColumnNamesList();
        sql.append(" WHERE ").append(whereIdExpression(idColumns));

        SqlStatement.Builder builder = new SqlStatement.Builder(sql.toString());
        for (int i = 0; i < idColumns.size(); i++) {
            Row.Id id = row.getId();
            builder.addParameter(new SqlParameterValue(id.getValueSqlType(i), id.getValue(i)));
        }

        return builder.build();
    }

    @Override
    public SqlStatement getDeleteAllStmt(Table table) {
        StringBuilder sql = new StringBuilder("TRUNCATE ").append(table.getFullName());
        SqlStatement.Builder builder = new SqlStatement.Builder(sql.toString());
        return builder.build();
    }

    @Override
    public SqlStatement getReadOneStmt(Row.Id eId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(eId.getContainerId().getFullName());

        List<String> idColumns = eId.getTable().getPrimaryKey().getColumnNamesList();
        sql.append(" WHERE ").append(whereIdExpression(idColumns));

        SqlStatement.Builder builder = new SqlStatement.Builder(sql.toString());
        for (int i = 0; i < idColumns.size(); i++) {
            builder.addParameter(new SqlParameterValue(eId.getValueSqlType(i), eId.getValue(i)));
        }

        return builder.build();
    }

    @Override
    public SqlStatement getReadNStmt(Table table, long offset, int limit) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(table.getFullName());
        sql.append(" LIMIT ? OFFSET ?");
        SqlStatement.Builder builder = new SqlStatement.Builder(sql.toString());
        builder.addParameter(new SqlParameterValue(Types.INTEGER, limit));
        builder.addParameter(new SqlParameterValue(Types.BIGINT, offset));
        return builder.build();
    }

    @Override
    public SqlStatement getReadAllStmt(Table table) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(table.getFullName());
        SqlStatement.Builder builder = new SqlStatement.Builder(sql.toString());
        return builder.build();
    }

    @Override
    public SqlStatement getReadFetchStmt(FetchParametersSource<Table> parameters) {
        return null;
    }

    @Override
    public SqlStatement getCountStmt(Table table) {
        StringBuilder sql = new StringBuilder("SELECT count(*) FROM ").append(table.getFullName());
        SqlStatement.Builder builder = new SqlStatement.Builder(sql.toString());
        return builder.build();
    }
}
