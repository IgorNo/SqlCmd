package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.Dao;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMdId;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;
import ua.com.nov.model.statement.SqlStatementSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class TableDao extends DataDefinitionDao<TableId, Table, Database.Id> {

    @Override
    public void create(Table value) throws SQLException {
        super.create(value);
        Dao<TableMdId, Index, TableId> dao = new IndexDao().setDataSource(getDataSource());
        for (Index index : value.getIndexList()) {
            dao.create(index);
        }
    }

    @Override
    public ResultSet getResultSet(TableId id) throws SQLException {
        return getDbMetaData().getTables(id.getCatalog(), id.getSchema(), id.getName(), new String[] {"TABLE"});
    }

    @Override
    protected ResultSet getResultSetAll(Database.Id id) throws SQLException {
        return getDbMetaData().getTables(null, null, null, new String[] {"TABLE"});
    }

    @Override
    protected Table rowMap(Database.Id id, ResultSet rs) throws SQLException {
        TableId tableId = new TableId(id, rs.getString("TABLE_NAME"),
                rs.getString("TABLE_CAT"), rs.getString("TABLE_SCHEM"));
        Table.Builder builder = new Table.Builder(tableId, rs.getString("TABLE_TYPE"));
        Collection<Column> columns = new ColumnDao().setDataSource(getDataSource()).readAll(tableId);
        builder.columns(columns);
        PrimaryKey pk = new PrimaryKeyDao().setDataSource(getDataSource()).readAll(tableId).get(0);
        builder.primaryKey(pk);
        List<ForeignKey> foreignKeys = new ForeignKeyDao().setDataSource(getDataSource()).readAll(tableId);
        builder.foreignKeys(foreignKeys);
        List<UniqueKey> uniqueKeys = new UniqueKeyDao().setDataSource(getDataSource()).readAll(tableId);
        builder.uniqueKeys(uniqueKeys);
        List<Index> indices = new IndexDao().setDataSource(getDataSource()).readAll(tableId);
        builder.indexList(indices);
        return builder.build();
    }

    @Override
    protected SqlStatementSource<TableId, Table, Database.Id> getSqlStmtSource(Database db) {
        return db.getTableSqlStmtSource();
    }
}
