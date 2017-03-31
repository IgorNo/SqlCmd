package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.datatype.DataType;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.statement.SqlStatementSource;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnDao extends DataDefinitionDao<Column.Id, Column, Table.Id> {

    @Override
    public void deleteAll(Table.Id container) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected ResultSet getResultSet(Column.Id id) throws SQLException {
        return getDbMetaData().getColumns(id.getTableId().getCatalog(), id.getTableId().getSchema(),
                id.getTableId().getName(), id.getName());
    }

    @Override
    protected ResultSet getResultSetAll(Table.Id id) throws SQLException {
        return getDbMetaData().getColumns(id.getCatalog(), id.getSchema(), id.getName(), null);
    }

    @Override
    protected Column rowMap(Table.Id key, ResultSet rs) throws SQLException {
        DataType dataType = key.getContainerId().getDb().getDataType(rs.getString("TYPE_NAME"));

        Column.Builder column = new Column.Builder(key, rs.getString("COLUMN_NAME"), dataType)
                .ordinalPosition(rs.getInt("ORDINAL_POSITION"))
                .size(rs.getInt("COLUMN_SIZE")).precision(rs.getInt("DECIMAL_DIGITS"))
                .nullable(rs.getInt("NULLABLE")).remarks(rs.getString("REMARKS"))
                .defaultValue(rs.getString("COLUMN_DEF"))
                .autoIncrement(rs.getString("IS_AUTOINCREMENT").equalsIgnoreCase("YES"));
                try {
                    column.generatedColumn(rs.getString("IS_GENERATEDCOLUMN").equalsIgnoreCase("YES"));
                } catch (SQLException e) {/*NOP*/}

        return  column.build();
    }

    @Override
    protected SqlStatementSource<Column.Id, Column, Table.Id> getSqlStmtSource(Database db) {
        return db.getColumnSqlStmtSource();
    }
}

