package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.statement.SqlStatementSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UniqueKeyDao extends MetaDataDao<UniqueKey.Id, UniqueKey, Table.Id> {

    @Override
    protected ResultSet getResultSet(String catalog, String schema, String tableName, String ignore) throws SQLException {
        return getDbMetaData().getIndexInfo(catalog, schema, tableName, true, false);
    }

    @Override
    protected AbstractDao.AbstractRowMapper<UniqueKey, Table.Id> getRowMapper(Table.Id id) {
        return new AbstractDao.AbstractRowMapper<UniqueKey, Table.Id>(id) {
            @Override
            public UniqueKey mapRow(ResultSet rs, int i) throws SQLException {
                UniqueKey.Builder index = new UniqueKey.Builder(rs.getString("INDEX_NAME"), id);
                do {
                    if (!rs.getString("INDEX_NAME").equalsIgnoreCase(index.getName())) {
                        rs.previous();
                        break;
                    }
                    index.addColumn(rs.getInt("ORDINAL_POSITION"), rs.getString("COLUMN_NAME"));
                } while (rs.next());
                return index.build();            }
        };
    }

    @Override
    protected SqlStatementSource<UniqueKey.Id, UniqueKey, Table.Id> getSqlStmtSource(Database db) {
        return db.getUniqueKeySqlStmtSource();
    }
    
}
