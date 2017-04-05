package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.statement.AbstractMetaDataSqlStatements;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.KeyCol;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IndexDao extends MetaDataDao<Index.Id, Index, Table.Id> {

    @Override
    protected ResultSet getResultSet(String catalog, String schema, String tableName, String ignore) throws SQLException {
        return getDbMetaData().getIndexInfo(catalog, schema, tableName, false, false);
    }

    @Override
    protected AbstractDao.AbstractRowMapper<Index, Table.Id> getRowMapper(Table.Id id) {
        return new AbstractDao.AbstractRowMapper<Index, Table.Id>(id) {
            @Override
            public Index mapRow(ResultSet rs, int i) throws SQLException {
                Index.Builder index = new Index.Builder(rs.getString("INDEX_NAME"), id)
                        .unique(!rs.getBoolean("NON_UNIQUE"));
                do {
                    if (!rs.getString("INDEX_NAME").equalsIgnoreCase(index.getName())) {
                        rs.previous();
                        break;
                    }
                    String ascDesc = null;
                    switch (rs.getString("ASC_OR_DESC")) {
                        case "A":
                            ascDesc = "ASC";
                            break;
                        case  "D":
                            ascDesc = "DESC";
                    }
                    KeyCol column = new KeyCol(rs.getString("COLUMN_NAME"), ascDesc);
                    index.addColumn(rs.getInt("ORDINAL_POSITION"), column);
                } while (rs.next());
                return index.build();
            }
        };
    }

    @Override
    protected AbstractMetaDataSqlStatements getSqlStmtSource(Database db) {
        return db.getIndexSqlStmtSource();
    }

}
