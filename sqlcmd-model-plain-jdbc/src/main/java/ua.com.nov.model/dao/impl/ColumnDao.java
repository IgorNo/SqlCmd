package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.datatype.DataType;
import ua.com.nov.model.entity.metadata.table.Column;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMdId;
import ua.com.nov.model.statement.SqlStatementSource;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnDao extends DataDefinitionDao<TableMdId, Column, TableId> {

    @Override
    public void deleteAll(TableId container) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected ResultSet getResultSet(TableMdId id) throws SQLException {
        return getDbMetaData().getColumns(id.getTableId().getCatalog(), id.getTableId().getSchema(),
                id.getTableId().getName(), id.getName());
    }

    @Override
    protected ResultSet getResultSetAll(TableId id) throws SQLException {
        return getDbMetaData().getColumns(id.getCatalog(), id.getSchema(), id.getName(), null);
    }

    @Override
    protected Column rowMap(TableId key, ResultSet rs) throws SQLException {
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
    protected SqlStatementSource<TableMdId, Column, TableId> getSqlStmtSource(Database db) {
        return db.getColumnSqlStmtSource();
    }
}

