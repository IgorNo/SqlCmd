package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.Dao;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.statement.SqlStatementSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.schema.Schema;
import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class TableDao extends MetaDataDao<Table.Id, Table, Schema.Id> {

    @Override
    public void create(Table value) throws DaoSystemException {
        super.create(value);
        Dao<Index.Id, Index, Table.Id> dao = new IndexDao().setDataSource(getDataSource());
        for (Index index : value.getIndexList()) {
            dao.create(index);
        }
    }

    @Override
    protected ResultSet getResultSet(String catalog, String schema, String ignore, String name) throws SQLException {
        return getDbMetaData().getTables(catalog, schema, name, new String[] {"TABLE"});
    }

    @Override
    protected AbstractDao.AbstractRowMapper<Table, Schema.Id> getRowMapper(Schema.Id id) {
        return new AbstractDao.AbstractRowMapper<Table, Schema.Id>(id) {
            @Override
            public Table mapRow(ResultSet rs, int i) throws SQLException {
                Table.Id tableId = new Table.Id(id, rs.getString("TABLE_NAME"));

                Table.Builder builder = new Table.Builder(tableId).type("TYPE_NAME");
                try {
                    Collection<Column> columns = new ColumnDao().setDataSource(getDataSource()).readAll(tableId);
                    builder.columns(columns);
                    List<ForeignKey> foreignKeys = new ForeignKeyDao().setDataSource(getDataSource()).readAll(tableId);
                    builder.addConstraintList(foreignKeys);
                    List<UniqueKey> uniqueKeys = new UniqueKeyDao().setDataSource(getDataSource()).readAll(tableId);
                    builder.addConstraintList(uniqueKeys);
                    PrimaryKey pk = new PrimaryKeyDao().setDataSource(getDataSource()).readAll(tableId).get(0);
                    builder.addConstraint(pk);
                    List<Index> indices = new IndexDao().setDataSource(getDataSource()).readAll(tableId);
                    builder.indexList(indices);
                } catch (DaoSystemException e) {
                    throw new SQLException("",e);
                }

                return builder.build();
            }
        };
    }

    @Override
    protected SqlStatementSource<Table.Id, Table, Schema.Id> getSqlStmtSource(Database db) {
        return db.getTableSqlStmtSource();
    }
}
