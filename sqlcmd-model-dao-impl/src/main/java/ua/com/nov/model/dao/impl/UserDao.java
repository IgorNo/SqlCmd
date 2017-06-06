package ua.com.nov.model.dao.impl;

import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.SqlExecutor;
import ua.com.nov.model.dao.statement.DataDefinitionSqlStmtSource;
import ua.com.nov.model.dao.statement.OptionsSqlStmtSource;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.grantee.User;
import ua.com.nov.model.entity.metadata.grantee.UserOptions;
import ua.com.nov.model.entity.metadata.server.Server;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao extends AbstractDao<User.Id, User, Server.Id> {

    @Override
    protected SqlExecutor<User> getExecutor(DataSource dataSource) {
        return new DDLSqlExecutor<>(dataSource);
    }

    @Override
    protected DataDefinitionSqlStmtSource<User.Id, User, Server.Id> getSqlStmtSource(Server db) {
        return db.getUserSqlStmtSource();
    }

    @Override
    protected AbstractRowMapper<User, Server.Id> getRowMapper(Server.Id id) {
        return new AbstractRowMapper<User, Server.Id>(id) {
            @Override
            public User mapRow(ResultSet rs, int i) throws SQLException {
                User.Id userId = new User.Id(id, rs.getString(1));
                OptionsSqlStmtSource<User.Id, User> stmtSource = id.getServer().getOptionsSqlStmtSource(userId.getMdName());
                List<UserOptions.Builder<? extends MetaDataOptions<User>>> options =
                        new RowMapperResultSetExtractor<>(stmtSource.getOptionsRowMapper()).extractData(rs);
                User user = new User(userId, options.get(0).build());
                return user;
            }
        };
    }
}
