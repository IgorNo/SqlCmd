package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.statement.DataDefinitionSqlStmtSource;
import ua.com.nov.model.entity.metadata.datatype.DbDataType;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.column.ColumnOptions;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnDao extends MetaDataDao<Column.Id, Column, Table.Id> {

    @Override
    protected ResultSet getResultSet(Table.Id id, String columnName)
            throws SQLException {
        return getDbMetaData().getColumns(id.getCatalog(), id.getSchema(), id.getName(), columnName);
    }

    @Override
    protected AbstractDao.AbstractRowMapper<Column, Table.Id> getRowMapper(Table.Id tableId) {
        return new AbstractDao.AbstractRowMapper<Column, Table.Id>(tableId) {
            @Override
            public Column mapRow(ResultSet rs, int i) throws SQLException {
                DbDataType dataType = tableId.getServer().getDataType(rs.getString("TYPE_NAME"));
                Column.Id id = new Column.Id(tableId, rs.getString("COLUMN_NAME"));

                Column.Builder builder = new Column.Builder(id, dataType)
                        .ordinalPosition(rs.getInt("ORDINAL_POSITION"))
                        .size(rs.getInt("COLUMN_SIZE")).precision(rs.getInt("DECIMAL_DIGITS"))
                        .nullable(rs.getInt("NULLABLE")).viewName(rs.getString("REMARKS"))
                        .defaultValue(rs.getString("COLUMN_DEF"))
                        .autoIncrement(rs.getString("IS_AUTOINCREMENT").equalsIgnoreCase("YES"));

                ColumnOptions.Builder<?> optionsBuilder =
                        (ColumnOptions.Builder<?>) new OptionsDao<Column.Id, Column>(getDataSource()).read(id);
                builder.options(optionsBuilder);
//                    rs.getString("IS_GENERATEDCOLUMN").equalsIgnoreCase("YES");

                return builder.build();
            }
        };
    }

    @Override
    protected DataDefinitionSqlStmtSource<Column.Id, Column, Table.Id> getSqlStmtSource(Server server) {
        return server.getColumnSqlStmtSource();
    }
}

