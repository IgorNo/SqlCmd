package ua.com.nov.model.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.SqlExecutor;
import ua.com.nov.model.dao.exception.DaoBusinessLogicException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.statement.DataDefinitionSqlStmtSource;
import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.grantee.user.User;
import ua.com.nov.model.entity.metadata.grantee.user.UserOptions;
import ua.com.nov.model.entity.metadata.server.Server;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao extends AbstractDao<Grantee.Id, User, Server.Id> {

    @Override
    protected SqlExecutor createExecutor(DataSource dataSource) {
        return new DDLSqlExecutor(dataSource);
    }

    @Override
    protected DataDefinitionSqlStmtSource<Grantee.Id, User, Server.Id> getSqlStmtSource(Server server) {
        return server.getUserSqlStmtSource();
    }

    @Override
    public User read(Grantee.Id eId) throws DaoSystemException {
        try {
            return super.read(eId);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoBusinessLogicException(String.format("%s '%s' doesn't exist in %s '%s'.\n",
                    eId.getMdName(), eId.getFullName(),
                    eId.getContainerId().getMdName(), eId.getContainerId().getFullName()));
        }
    }


    @Override
    protected AbstractRowMapper<User, Server.Id> getRowMapper(Server.Id id) {
        return new AbstractRowMapper<User, Server.Id>(id) {
            @Override
            public User mapRow(ResultSet rs, int i) throws SQLException {
                UserOptions options = (UserOptions) id.getServer().getUserOptionsSqlStmSource().getOptionsRowMapper()
                        .mapRow(rs, 0)
                        .build();
                return id.getServer().getUserBuilder(id, rs.getString(1), options).build();
            }
        };
    }
}
