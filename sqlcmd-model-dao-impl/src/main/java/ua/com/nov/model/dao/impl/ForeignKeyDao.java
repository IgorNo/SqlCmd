package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.statement.DataDefinitionSqlStmtSource;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForeignKeyDao extends MetaDataDao<TableMd.Id, ForeignKey, Table.Id> {

    public ForeignKeyDao() {
    }

    public ForeignKeyDao(DataSource dataSource) {
        super(dataSource);
    }

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
                    Table.Id tableIdPk = new Table.Id(id.getContainerId().getContainerId(), rs.getString("PKTABLE_NAME"),
                            rs.getString("PKTABLE_CAT"), rs.getString("PKTABLE_SCHEM"));
                    Column.Id pkColumn = new Column.Id(tableIdPk, rs.getString("PKCOLUMN_NAME"));
                    fk.addColumnPair(rs.getInt("KEY_SEQ"), rs.getString("FKCOLUMN_NAME"), pkColumn);
                    fk.deleteRule(rs.getInt("DELETE_RULE")).updateRule(rs.getInt("UPDATE_RULE"));
                } while (rs.next());
                return fk.build();
            }
        };
    }

    @Override
    protected DataDefinitionSqlStmtSource<TableMd.Id, ForeignKey, Table.Id> getSqlStmtSource(Server db) {
        return db.getTableMdSqlStmtSource();
    }

}
