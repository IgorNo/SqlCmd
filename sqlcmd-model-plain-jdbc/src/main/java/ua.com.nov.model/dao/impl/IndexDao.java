package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMdId;
import ua.com.nov.model.entity.metadata.table.column.KeyCol;
import ua.com.nov.model.statement.AbstractIndexSqlStatements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class IndexDao extends DataDefinitionDao<TableMdId, Index, Table.Id> {

    @Override
    protected ResultSet getResultSetAll(Table.Id id) throws SQLException {
        return  getDbMetaData().getIndexInfo(id.getCatalog(), id.getSchema(), id.getName(), false, false);
    }

    @Override
    public Index read(TableMdId id) throws SQLException {
        Collection<Index> indices = readAll(id.getTableId());
        for (Index index : indices) {
            if (index.getId().equals(id)) return index;
        }
        throw new IllegalArgumentException(String.format("Index '%s' doesn't exist in table '%s'.",
                id.getName(), id.getTableId().getFullName()));
    }

    @Override
    protected Index rowMap(Table.Id tableId, ResultSet rs) throws SQLException {
        Index.Builder index = new Index.Builder(rs.getString("INDEX_NAME"), tableId)
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
        return (Index) index.build();
    }

    @Override
    protected AbstractIndexSqlStatements getSqlStmtSource(Database db) {
        return db.getIndexSqlStmtSource();
    }

}
