package ua.com.nov.model.entity.metadata.server;

import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.dao.statement.*;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.datatype.DataTypes;
import ua.com.nov.model.entity.metadata.datatype.DbDataType;
import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.grantee.privelege.HyperSqlPrivilege;
import ua.com.nov.model.entity.metadata.grantee.privelege.Privilege;
import ua.com.nov.model.entity.metadata.grantee.user.HyperSqlUserOptions;
import ua.com.nov.model.entity.metadata.grantee.user.User;
import ua.com.nov.model.entity.metadata.grantee.user.UserOptions;
import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.column.ColumnOptions;
import ua.com.nov.model.entity.metadata.table.column.HyperSqlColumnOptions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class HsqldbServer extends Server {

    public HsqldbServer(String dbUrl) {
        super(dbUrl);
        getTypesMap().put(DataTypes.LONGVARCHAR, "LONGVARCHAR");
        List<DbDataType> dataTypeList = new ArrayList<>();
        dataTypeList.add(new DbDataType.Builder("LONGVARCHAR", Types.LONGVARCHAR).build());
        addDataTypes(dataTypeList);
    }

    @Override
    public ColumnOptions.Builder<HyperSqlColumnOptions> createColumnOptions() {
        return new HyperSqlColumnOptions.Builder();
    }

    @Override
    public String convert(String parameter) {
        if (parameter != null) return parameter.toUpperCase();
        return parameter;
    }

    @Override
    public AbstractDatabaseMdSqlStatements getDatabaseSqlStmtSource() {
        return new AbstractDatabaseMdSqlStatements<Database.Id, Database, Server.Id>() {

            @Override
            public SqlStatement getCreateStmt(Database db) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SqlStatement getUpdateStmt(Database db) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SqlStatement getDeleteStmt(Database.Id db) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SqlStatement getRenameStmt(Database db, String newName) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public <I extends TableMd.Id, E extends TableMd> AbstractTableMdSqlStatements<I, E> getTableMdSqlStmtSource() {
        return new AbstractTableMdSqlStatements<I, E>() {
            @Override
            public SqlStatement getRenameStmt(E entity, String newName) {
                return new SqlStatement.Builder(String.format("ALTER %s %s RENAME TO %s",
                        entity.getMdName(), entity.getName(), newName)).build();
            }
        };
    }

    @Override
    public AbstractTableMdSqlStatements<Column.Id, Column> getColumnSqlStmtSource() {
        return new AbstractTableMdSqlStatements<Column.Id, Column>() {
            @Override
            public SqlStatement getRenameStmt(Column col, String newName) {
                return new SqlStatement.Builder(String.format("ALTER TABLE %s ALTER COLUMN %s RENAME TO %s",
                        col.getTableId().getFullName(), col.getName(), newName)).build();
            }

            @Override
            public SqlStatement getUpdateStmt(Column col) {
                String alterSql = String.format("ALTER TABLE %s ALTER COLUMN %s",
                        col.getTableId().getFullName(), col.getName());
                String sql = "";
                if (col.getDataType() != null) {
                    sql = alterSql + " SET DATA TYPE " + col.getFullTypeDeclaration() + ";\n";
                }
                for (String set : col.getOptions().getUpdateOptionsDefinition()) {
                    sql += alterSql + " " + set + ";\n";
                }
                sql += getCommentStmt(col);
                return new SqlStatement.Builder(sql).build();
            }
        };
    }

    @Override
    public OptionsSqlStmtSource<Grantee.Id, User> getUserOptionsSqlStmSource() {
        return new OptionsSqlStmtSource<Grantee.Id, User>() {
            public static final String sql = "SELECT * FROM PUBLIC.INFORMATION_SCHEMA.SYSTEM_USERS";
            @Override
            public SqlStatement getReadAllOptionsStmt() {
                return new SqlStatement.Builder(sql).build();
            }

            @Override
            public SqlStatement getReadOptionsStmt(Grantee.Id eId) {
                return new SqlStatement.Builder(sql + " WHERE USER_NAME = '" + eId.getName() + "'").build();
            }

            @Override
            public RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<User>>> getOptionsRowMapper() {
                return new RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<User>>>() {
                    @Override
                    public HyperSqlUserOptions.Builder mapRow(ResultSet rs, int i)
                            throws SQLException {
                        HyperSqlUserOptions.Builder builder = new HyperSqlUserOptions.Builder()
                                .password(rs.getString("PASSWORD_DIGEST"))
                                .initialSchema(rs.getString("INITIAL_SCHEMA"));
                        if (rs.getBoolean("ADMIN")) builder.admin();
                        if (rs.getString("AUTHENTICATION").equals("LOCAL")) builder.authenticationLocal();
                        return builder;
                    }
                };
            }
        };
    }

    @Override
    public AbstractPrivilegeStatements getPrivelegeStmtSource() {
        return new AbstractPrivilegeStatements() {
            @Override
            public SqlStatement getCreateStmt(Privilege privilege) {
                return new SqlStatement.Builder(super.getCreateStmt(privilege).getSql()).build();
            }
        };
    }

    @Override
    public User.Builder getUserBuilder(Id id, String name, UserOptions options) {
        return new User.Builder(id, name, options) {
            @Override
            public User build() {
                id = new User.Id(serverId, name);
                for (Grantee grantee : grantees) {
                    for (Privilege privilege : grantee.getAllPrivileges()) {
                        if (privilege.isWithGrantOptions())
                            addPrivelege(new HyperSqlPrivilege.Builder((HyperSqlPrivilege) privilege));
                    }
                }
                return new User(this);
            }
        };
    }
}
