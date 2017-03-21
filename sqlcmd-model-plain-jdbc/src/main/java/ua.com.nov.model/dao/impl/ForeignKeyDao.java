package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMdId;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.statement.SqlStatementSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class ForeignKeyDao extends DataDefinitionDao<TableMdId, ForeignKey, TableId> {

    @Override
    public ForeignKey read(TableMdId key) throws SQLException {
        Collection<ForeignKey> foreignKeys = readAll(key.getTableId());
        for (ForeignKey fk : foreignKeys) {
            if (fk.getId().equals(key)) return fk;
        }
        throw new IllegalArgumentException(String.format("Foreign Key '%s' doesn't exist in table '%s'.",
                key.getName(), key.getTableId().getFullName()));
    }

    @Override
    protected ResultSet getResultSetAll(TableId id) throws SQLException {
        return  getDbMetaData().getImportedKeys(id.getCatalog(), id.getSchema(),
                id.getName());
    }

    @Override
    public ForeignKey rowMap(TableId tableId, ResultSet rs) throws SQLException {
        ForeignKey.Builder fk = new ForeignKey.Builder(rs.getString("FK_NAME"), tableId);
        do {
            if (!rs.getString("FK_NAME").equalsIgnoreCase(fk.getName())) {
                rs.previous();
                break;
            }
            TableId tableIdPk = new TableId(tableId.getDb().getId(), rs.getString("PKTABLE_NAME"),
                    rs.getString("PKTABLE_CAT"), rs.getString("PKTABLE_SCHEM"));
            TableId tableIdFk = new TableId(tableId.getDb().getId(), rs.getString("FKTABLE_NAME"),
                    rs.getString("FKTABLE_CAT"), rs.getString("FKTABLE_SCHEM"));
            TableMdId pkColumn = new TableMdId(tableIdPk, rs.getString("PKCOLUMN_NAME"));
            fk.addColumnPair(rs.getInt("KEY_SEQ"), rs.getString("FKCOLUMN_NAME"), pkColumn);
            fk.deleteRule(rs.getInt("DELETE_RULE")).updateRule(rs.getInt("UPDATE_RULE"));
        } while (rs.next());
        return fk.build();
    }

    @Override
    protected SqlStatementSource<TableMdId, ForeignKey, TableId> getSqlStmtSource(Database db) {
        return db.getForeignKeySqlStmtSource();
    }

}
