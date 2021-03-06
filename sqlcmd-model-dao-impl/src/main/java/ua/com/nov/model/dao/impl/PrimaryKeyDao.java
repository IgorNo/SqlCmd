package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.statement.SqlStatementSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrimaryKeyDao extends MetaDataDao<PrimaryKey.Id, PrimaryKey, Table.Id> {
    @Override
    protected ResultSet getResultSet(String catalog, String schema, String tableName, String ignore) throws SQLException {
        return getDbMetaData().getPrimaryKeys(catalog, schema, tableName);
    }

    @Override
    protected AbstractDao.AbstractRowMapper<PrimaryKey, Table.Id> getRowMapper(Table.Id id) {
        return new AbstractDao.AbstractRowMapper<PrimaryKey, Table.Id>(id) {
            @Override
            public PrimaryKey mapRow(ResultSet rs, int i) throws SQLException {
                PrimaryKey.Builder pk = new PrimaryKey.Builder(rs.getString("PK_NAME"), id);
                pk.addColumn(rs.getInt("KEY_SEQ"), rs.getString("COLUMN_NAME"));
                while (rs.next()) {
                    pk.addColumn(rs.getInt("KEY_SEQ"), rs.getString("COLUMN_NAME"));
                }
                return pk.build();
            }
        };
    }

    @Override
    protected SqlStatementSource<PrimaryKey.Id, PrimaryKey, Table.Id> getSqlStmtSource(Database db) {
        return db.getPrimaryKeySqlStmtSource();
    }

}
