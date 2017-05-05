package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.statement.DataDefinitionSqlStmtSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ForeignKeyDao extends MetaDataDao<ForeignKey.Id, ForeignKey, Table.Id> {

    @Override
    protected ResultSet getResultSet(Table.Id id, String ignore) throws SQLException {
        return getDbMetaData().getImportedKeys(id.getCatalog(), id.getSchema(), id.getName());
    }

    @Override
    protected AbstractDao.AbstractRowMapper<ForeignKey, Table.Id> getRowMapper(Table.Id id) {
        return new AbstractDao.AbstractRowMapper<ForeignKey, Table.Id>(id) {
            @Override
            public ForeignKey mapRow(ResultSet rs, int i) throws SQLException {
                ForeignKey.Builder fk = new ForeignKey.Builder(rs.getString("FK_NAME"), id);
                do {
                    if (!rs.getString("FK_NAME").equalsIgnoreCase(fk.getName())) {
                        rs.previous();
                        break;
                    }
                    Table.Id tableIdPk = new Table.Id(id.getDb().getId(), rs.getString("PKTABLE_NAME"),
                            rs.getString("PKTABLE_CAT"), rs.getString("PKTABLE_SCHEM"));
                    Table.Id tableIdFk = new Table.Id(id.getDb().getId(), rs.getString("FKTABLE_NAME"),
                            rs.getString("FKTABLE_CAT"), rs.getString("FKTABLE_SCHEM"));
                    Column.Id pkColumn = new Column.Id(tableIdPk, rs.getString("PKCOLUMN_NAME"));
                    fk.addColumnPair(rs.getInt("KEY_SEQ"), rs.getString("FKCOLUMN_NAME"), pkColumn);
                    fk.deleteRule(rs.getInt("DELETE_RULE")).updateRule(rs.getInt("UPDATE_RULE"));
                } while (rs.next());
                return fk.build();
            }
        };
    }

    @Override
    protected DataDefinitionSqlStmtSource<ForeignKey.Id, ForeignKey, Table.Id> getSqlStmtSource(Database db) {
        return db.getForeignKeySqlStmtSource();
    }

}
