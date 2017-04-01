package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.statement.SqlStatementSource;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrimaryKeyDao extends DataDefinitionDao<PrimaryKey.Id, PrimaryKey, Table.Id> {

    @Override
    protected ResultSet getResultSet(PrimaryKey.Id id) throws SQLException {
        return getResultSetAll(id.getTableId());
    }

    @Override
    protected ResultSet getResultSetAll(Table.Id id) throws SQLException {
        return  getDbMetaData().getPrimaryKeys(id.getCatalog(), id.getSchema(),
                id.getName());
    }

    @Override
    protected PrimaryKey rowMap(Table.Id tableId, ResultSet rs) throws SQLException {
        PrimaryKey.Builder pk = new PrimaryKey.Builder(rs.getString("PK_NAME"), tableId);
        pk.addColumn(rs.getInt("KEY_SEQ"), rs.getString("COLUMN_NAME"));
        while (rs.next()) {
            pk.addColumn(rs.getInt("KEY_SEQ"), rs.getString("COLUMN_NAME"));
        }
        return pk.build();
    }

    @Override
    protected SqlStatementSource<PrimaryKey.Id, PrimaryKey, Table.Id> getSqlStmtSource(Database db) {
        return db.getPrimaryKeySqlStmtSource();
    }

}