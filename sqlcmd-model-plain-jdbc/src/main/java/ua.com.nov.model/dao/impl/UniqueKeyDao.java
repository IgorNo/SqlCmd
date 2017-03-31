package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;
import ua.com.nov.model.statement.SqlStatementSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class UniqueKeyDao extends DataDefinitionDao<UniqueKey.Id, UniqueKey, Table.Id> {

    @Override
    public UniqueKey read(UniqueKey.Id key) throws SQLException {
        Collection<UniqueKey> foreignKeys = readAll(key.getTableId());
        for (UniqueKey fk : foreignKeys) {
            if (fk.getId().equals(key)) return fk;
        }
        throw new IllegalArgumentException(String.format("Unique Key '%s' doesn't exist in table '%s'.",
                key.getName(), key.getTableId().getFullName()));
    }

    @Override
    protected ResultSet getResultSetAll(Table.Id id) throws SQLException {
        return  getDbMetaData().getIndexInfo(id.getCatalog(), id.getSchema(), id.getName(), true, false);
    }

    @Override
    protected UniqueKey rowMap(Table.Id tableId, ResultSet rs) throws SQLException {
        UniqueKey.Builder index = new UniqueKey.Builder(rs.getString("INDEX_NAME"), tableId);
        do {
            if (!rs.getString("INDEX_NAME").equalsIgnoreCase(index.getName())) {
                rs.previous();
                break;
            }
            index.addColumn(rs.getInt("ORDINAL_POSITION"), rs.getString("COLUMN_NAME"));
        } while (rs.next());
        return index.build();
    }

    @Override
    protected SqlStatementSource<UniqueKey.Id, UniqueKey, Table.Id> getSqlStmtSource(Database db) {
        return db.getUniqueKeySqlStmtSource();
    }
    
}
