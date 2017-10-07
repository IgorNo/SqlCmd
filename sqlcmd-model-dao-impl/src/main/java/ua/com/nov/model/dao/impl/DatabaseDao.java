package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.DataDefinitionDao;
import ua.com.nov.model.dao.SqlExecutor;
import ua.com.nov.model.dao.exception.DAOSystemException;
import ua.com.nov.model.dao.statement.AbstractDatabaseMdSqlStatements;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.database.Database.Id;
import ua.com.nov.model.entity.metadata.server.Server;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class DatabaseDao
        extends AbstractDao<Database.Id, Database, Server.Id> implements DataDefinitionDao<Database.Id, Database> {

    public DatabaseDao() {
    }

    public DatabaseDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void createIfNotExist(Database db) throws DAOSystemException {
        getExecutor().executeUpdateStmt(getSqlStmtSource(db.getServer()).getCreateIfNotExistsStmt(db));
    }

    @Override
    public Database read(Id eId) throws DAOSystemException {
        Database.Id dbId = new Database.Id(eId.getServer(), eId.getName());
        MetaDataOptions.Builder<?> builder = null;
        try {
            builder = new OptionsDao<Id, Database>(getDataSource()).read(dbId);
        } catch (SQLException e) {
            throw new DAOSystemException(e.getMessage());
        }
        if (builder != null)
            return new Database(eId.getServer(), dbId.getName(), builder.build());
        else
            return new Database(eId.getServer(), dbId.getName());
    }

    @Override
    public List<Database> readAll(Server.Id cId) throws DAOSystemException {
        return super.readAll(cId);
    }

    @Override
    public void deleteIfExist(Database.Id eId) throws DAOSystemException {
        getExecutor().executeUpdateStmt(getSqlStmtSource(eId.getServer()).getDeleteIfExistStmt(eId));
    }

    @Override
    protected SqlExecutor createExecutor(DataSource dataSource) {
        return new DDLSqlExecutor(dataSource);
    }

    @Override
    protected AbstractRowMapper getRowMapper(Server.Id id) {
        return new AbstractRowMapper(id) {
            @Override
            public Database mapRow(ResultSet rs, int i) throws SQLException {
                Database.Id dbId = new Database.Id(id.getServer(), rs.getString(1));
                MetaDataOptions.Builder<?> builder = new OptionsDao<Id, Database>(getDataSource()).read(dbId);
                return new Database(id.getServer(), dbId.getName(), builder.build());
            }
        };
    }

    @Override
    protected AbstractDatabaseMdSqlStatements getSqlStmtSource(Server server) {
        return server.getDatabaseSqlStmtSource();
    }
}
