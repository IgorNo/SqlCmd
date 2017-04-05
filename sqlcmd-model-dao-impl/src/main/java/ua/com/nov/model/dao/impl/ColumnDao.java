package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.statement.SqlStatementSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.datatype.DataType;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnDao extends MetaDataDao<Column.Id, Column, Table.Id> {

    @Override
    protected ResultSet getResultSet(String catalog, String schema, String tableName, String columnName)
            throws SQLException {
        return getDbMetaData().getColumns(catalog, schema, tableName, columnName);
    }

    @Override
    protected AbstractDao.AbstractRowMapper<Column, Table.Id> getRowMapper(Table.Id id) {
        return new AbstractDao.AbstractRowMapper<Column, Table.Id>(id) {
            @Override
            public Column mapRow(ResultSet rs, int i) throws SQLException {
                DataType dataType = id.getContainerId().getDb().getDataType(rs.getString("TYPE_NAME"));

                Column.Builder column = new Column.Builder(id, rs.getString("COLUMN_NAME"), dataType)
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
        };
    }

    @Override
    protected SqlStatementSource<Column.Id, Column, Table.Id> getSqlStmtSource(Database db) {
        return db.getColumnSqlStmtSource();
    }
}

