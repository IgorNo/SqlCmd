package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.SqlExecutor;
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
    protected SqlExecutor<User> getExecutor(DataSource dataSource) {
        return new DDLSqlExecutor<>(dataSource);
    }

    @Override
    protected DataDefinitionSqlStmtSource<Grantee.Id, User, Server.Id> getSqlStmtSource(Server server) {
        return server.getUserSqlStmtSource();
    }

    @Override
    protected AbstractRowMapper<User, Server.Id> getRowMapper(Server.Id id) {
        return new AbstractRowMapper<User, Server.Id>(id) {
            @Override
            public User mapRow(ResultSet rs, int i) throws SQLException {
                User.Id userId = new User.Id(id, rs.getString("USER_NAME"));
                UserOptions options = (UserOptions) id.getServer().getUserOptionsSqlStmSource().getOptionsRowMapper()
                        .mapRow(rs, 0)
                        .build();
                return new User.Builder(userId, options).build();
            }
        };
    }
}
