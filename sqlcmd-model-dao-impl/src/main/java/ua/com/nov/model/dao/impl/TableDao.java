package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.Dao;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.statement.AbstractDatabaseMdSqlStatements;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.schema.Schema;
import ua.com.nov.model.entity.metadata.server.MySqlServer;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.MySqlTableOptions;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class TableDao extends MetaDataDao<Table.Id, Table, Schema.Id> {

    public TableDao() {
    }

    public TableDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void create(Table entity) throws DaoSystemException {
        super.create(entity);
        Dao<Index.Id, Index> dao = new IndexDao(getDataSource());
        for (Index index : entity.getIndexList()) {
            dao.create(index);
        }
    }

    @Override
    protected ResultSet getResultSet(Schema.Id id, String name) throws SQLException {
        return getDbMetaData().getTables(id.getCatalog(), id.getSchema(), name, id.getServer().getTableTypes());
    }

    @Override
    protected AbstractDao.AbstractRowMapper<Table, Schema.Id> getRowMapper(Schema.Id id) {
        return new AbstractDao.AbstractRowMapper<Table, Schema.Id>(id) {
            @Override
            public Table mapRow(ResultSet rs, int i) throws SQLException {
                Table.Id tableId = new Table.Id(id, rs.getString("TABLE_NAME"));

                Table.Builder builder = new Table.Builder(tableId).type(rs.getString("TABLE_TYPE"))
                        .viewName(rs.getString("REMARKS"));
                try {
                    MetaDataOptions.Builder<?> optionsBuilder = new OptionsDao<Table.Id, Table>(getDataSource())
                            .read(tableId);
                    if (optionsBuilder != null) {
                        Optional<Table> options = optionsBuilder.build();
                        builder.options(options);
                        if (tableId.getServer().getClass() == MySqlServer.class) {
                            builder.viewName(((MySqlTableOptions) options).getComment());
                        }
                    }
                    Collection<Column> columns = new ColumnDao(getDataSource()).readAll(tableId);
                    builder.columns(columns);
                    List<ForeignKey> foreignKeys = new ForeignKeyDao(getDataSource()).readAll(tableId);
                    builder.addConstraintList(foreignKeys);
                    List<UniqueKey> uniqueKeys = new UniqueKeyDao(getDataSource()).readAll(tableId);
                    builder.addConstraintList(uniqueKeys);
                    List<PrimaryKey> pkList = new PrimaryKeyDao(getDataSource()).readAll(tableId);
                    PrimaryKey pk = null;
                    if (pkList.size() == 1) {
                        pk = pkList.get(0);
                        builder.addConstraint(pk);
                    }
                    List<Index> indices = new IndexDao(getDataSource()).readAll(tableId);
                    builder.indexList(indices);
                } catch (DaoSystemException e) {
                    throw new SQLException("", e);
                }
                return builder.build();
            }
        };
    }

    @Override
    protected AbstractDatabaseMdSqlStatements getSqlStmtSource(Server db) {
        return db.getTableSqlStmtSource();
    }
}
