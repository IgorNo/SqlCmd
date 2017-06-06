package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.statement.DataDefinitionSqlStmtSource;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UniqueKeyDao extends MetaDataDao<TableMd.Id, UniqueKey, Table.Id> {

    @Override
    protected ResultSet getResultSet(Table.Id id, String ignore) throws SQLException {
        return getDbMetaData().getIndexInfo(id.getCatalog(), id.getSchema(), id.getName(), true, false);
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
    protected DataDefinitionSqlStmtSource<TableMd.Id, UniqueKey, Table.Id> getSqlStmtSource(Server db) {
        return db.getTableMdSqlStmtSource();
    }
    
}
