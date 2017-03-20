package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.Column;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.statement.SqlStatementSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class TableDao extends DataDefinitionDao<TableId, Table, Database.DbId> {

    @Override
    public ResultSet getResultSet(TableId id) throws SQLException {
        return getDbMetaData().getTables(id.getCatalog(), id.getSchema(), id.getName(), new String[] {"TABLE"});
    }

    @Override
    protected ResultSet getResultSetAll(Database.DbId dbId) throws SQLException {
        return getDbMetaData().getTables(null, null, null, new String[] {"TABLE"});
    }

    @Override
    protected Table rowMap(Database.DbId dbId, ResultSet rs) throws SQLException {
        TableId tableId = new TableId(dbId, rs.getString("TABLE_NAME"),
                rs.getString("TABLE_CAT"), rs.getString("TABLE_SCHEM"));
        Table.Builder builder = new Table.Builder(tableId, rs.getString("TABLE_TYPE"));
        Collection<Column> columns = new ColumnDao().setDataSource(getDataSource()).readAll(tableId);
        builder.columns(columns);
        PrimaryKey pk = new PrimaryKeyDao().setDataSource(getDataSource()).readAll(tableId).get(0);
        builder.primaryKey(pk);
        return builder.build();
    }

    @Override
    protected SqlStatementSource<TableId, Table, Database.DbId> getSqlStmtSource(Database db) {
        return db.getTableSqlStmtSource();
    }
}
