package ua.com.nov.model.dao.statement;

import org.springframework.jdbc.core.SqlParameterValue;
import ua.com.nov.model.dao.fetch.FetchParameter;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;

import java.sql.Types;
import java.util.List;

public abstract class AbstractDataStmtSource<E extends AbstractRow> implements DataManipulationSqlStmtSource<AbstractRow.Id, E, Table> {

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
    public SqlStatement getCreateStmt(E row) {
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(row.getTable().getFullName()).append(" (");

        List<Column> columns = row.getTable().getColumns();
        String s = "";
        for (Column column : columns) {
            if (!(column.isAutoIncrement() || column.isGenerated())) {
                sql.append(s).append(column.getName());
                s = ", ";
            }
        }
        sql.append(") VALUES ");
        if (s.isEmpty()) return null;

        s = "(";
        for (Column column : columns) {
            if (!(column.isAutoIncrement() || column.isGenerated())) {
                sql.append(s).append('?');
                s = ", ";
            }
        }
        sql.append(')');

        SqlStatement.Builder builder = new SqlStatement.Builder(sql.toString());
        for (int i = 1; i <= columns.size(); i++) {
            if (!(columns.get(i - 1).isAutoIncrement() || columns.get(i - 1).isGenerated())) {
                builder.addParameter(new SqlParameterValue(row.getValueSqlType(i), row.getValue(i)));
            }
        }
        return builder.build();
    }

    @Override
    public SqlStatement getUpdateStmt(E row) {
        StringBuilder sql = new StringBuilder("UPDATE ").append(row.getTable().getFullName()).append(" SET ");

        List<Column> columns = row.getTable().getColumns();
        List<String> idColumns = row.getTable().getPrimaryKey().getColumnNamesList();
        String s = "";
        for (Column column : columns) {
            if (!(idColumns.contains(column.getName()) || column.isGenerated() || column.isAutoIncrement())) {
                sql.append(s).append(column.getName()).append(" = ?");
                s = ", ";
            }
        }
        if (s.isEmpty()) return null;

        sql.append(" WHERE ").append(whereIdExpression(idColumns));

        SqlStatement.Builder builder = new SqlStatement.Builder(sql.toString());
        for (int i = 1; i <= columns.size(); i++) {
            if (!(idColumns.contains(columns.get(i - 1).getName()) || columns.get(i - 1).isGenerated()
                    || columns.get(i - 1).isAutoIncrement())) {
                builder.addParameter(new SqlParameterValue(row.getValueSqlType(i), row.getValue(i)));
            }
        }
        for (int i = 1; i <= idColumns.size(); i++) {
            AbstractRow.Id id = row.getId();
            builder.addParameter(new SqlParameterValue(id.getValueSqlType(i), id.getValue(i)));
        }

        return builder.build();
    }

    @Override
    public SqlStatement getDeleteStmt(AbstractRow.Id id) {
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(id.getTable().getFullName());

        List<String> idColumns = id.getTable().getPrimaryKey().getColumnNamesList();
        sql.append(" WHERE ").append(whereIdExpression(idColumns));

        SqlStatement.Builder builder = new SqlStatement.Builder(sql.toString());
        for (int i = 1; i <= idColumns.size(); i++) {
            builder.addParameter(new SqlParameterValue(id.getValueSqlType(i), id.getValue(i)));
        }

        return builder.build();
    }

    @Override
    public SqlStatement getDeleteAllStmt(Table table) {
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(table.getFullName());
        SqlStatement.Builder builder = new SqlStatement.Builder(sql.toString());
        return builder.build();
    }

    @Override
    public SqlStatement getReadOneStmt(AbstractRow.Id eId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(eId.getContainerId().getFullName());

        List<String> idColumns = eId.getTable().getPrimaryKey().getColumnNamesList();
        sql.append(" WHERE ").append(whereIdExpression(idColumns));

        SqlStatement.Builder builder = new SqlStatement.Builder(sql.toString());
        for (int i = 1; i <= idColumns.size(); i++) {
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
    public SqlStatement getReadFetchStmt(Table cId, FetchParameter[] parameters) {
        return null;
    }

    @Override
    public SqlStatement getCountStmt(Table table) {
        StringBuilder sql = new StringBuilder("SELECT count(*) FROM ").append(table.getFullName());
        SqlStatement.Builder builder = new SqlStatement.Builder(sql.toString());
        return builder.build();
    }
}
