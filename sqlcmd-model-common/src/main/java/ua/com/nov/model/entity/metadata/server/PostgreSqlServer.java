package ua.com.nov.model.entity.metadata.server;

import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.dao.statement.AbstractDatabaseMdSqlStatements;
import ua.com.nov.model.dao.statement.AbstractTableMdSqlStatements;
import ua.com.nov.model.dao.statement.OptionsSqlStmtSource;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.database.PostgreSqlDbOptions;
import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.grantee.user.PostgreSqlUserOptions;
import ua.com.nov.model.entity.metadata.grantee.user.User;
import ua.com.nov.model.entity.metadata.grantee.user.UserOptions;
import ua.com.nov.model.entity.metadata.table.PostgreSqlTableOptions;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.column.ColumnOptions;
import ua.com.nov.model.entity.metadata.table.column.PostgresSqlColumnOptions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PostgreSqlServer extends Server {

    public PostgreSqlServer(String dbUrl) {
        super(dbUrl);
        getTypesMap().put(JdbcDataTypes.LONGVARCHAR, "text");
    }

    @Override
    public String convert(String parameter) {
        if (parameter != null) return parameter.toLowerCase();
        return parameter;
    }

    @Override
    public ColumnOptions.Builder<? extends ColumnOptions> createColumnOptions() {
        return new PostgresSqlColumnOptions.Builder();
    }

    @Override
    public String[] getTableTypes() {
        if (tableTypes != null) {
            List<String> result = new LinkedList<>();
            for (String type : tableTypes) {
                if (type.toUpperCase().contains("TABLE") || type.toUpperCase().contains("VIEW"))
                    result.add(type);
            }
            return result.toArray(new String[0]);
        } else {
            return null;
        }
    }

    @Override
    public AbstractDatabaseMdSqlStatements getDatabaseSqlStmtSource() {
        return new AbstractDatabaseMdSqlStatements<Database.Id, Database, Server.Id>() {
            @Override
            public SqlStatement getReadAllStmt(Server.Id cId) {
                return new SqlStatement.Builder("SELECT datname FROM pg_database WHERE datistemplate = false")
                        .build();
            }

        };
    }

    @Override
    protected OptionsSqlStmtSource<Database.Id, Database> getDatabaseOptionsSqlStmtSource() {
        return new OptionsSqlStmtSource<Database.Id, Database>() {
            @Override
            public SqlStatement getReadOptionsStmt(Database.Id eId) {
                return new SqlStatement.Builder(
                        "SELECT pg_encoding_to_char(encoding), datcollate, datctype, datistemplate, datallowconn, " +
                                "datconnlimit, rolname, spcname\n" +
                                "FROM pg_catalog.pg_database\n" +
                                "LEFT JOIN pg_catalog.pg_authid ON datdba = pg_catalog.pg_authid.oid\n" +
                                "LEFT JOIN pg_catalog.pg_tablespace ON dattablespace = pg_catalog.pg_tablespace.oid\n" +
                                "WHERE datname = " + "'" + eId.getName() + "'").build();
            }

            @Override
            public RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<Database>>> getOptionsRowMapper() {
                return new RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<Database>>>() {
                    @Override
                    public PostgreSqlDbOptions.Builder mapRow(ResultSet rs, int i) throws SQLException {
                        return new PostgreSqlDbOptions.Builder()
                                .encoding(rs.getString(1)).lcCollate(rs.getString(2))
                                .lcType(rs.getString(3)).isTemplate(rs.getBoolean(4))
                                .allowConn(rs.getBoolean(5)).connLimit(rs.getInt(6))
                                .owner(rs.getString(7)).tableSpace(rs.getString(8));
                    }
                };
            }
        };
    }

    @Override
    public OptionsSqlStmtSource<Grantee.Id, User> getUserOptionsSqlStmSource() {
        return new OptionsSqlStmtSource<Grantee.Id, User>() {
            @Override
            public SqlStatement getReadAllOptionsStmt() {
                return null;
            }

            @Override
            public SqlStatement getReadOptionsStmt(Grantee.Id eId) {
                return null;
            }

            @Override
            public RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<User>>> getOptionsRowMapper() {
                return null;
            }
        };
    }

    @Override
    protected OptionsSqlStmtSource<Table.Id, Table> getTableOptionsSqlStmtSource() {
        return new OptionsSqlStmtSource<Table.Id, Table>() {
            @Override
            public SqlStatement getReadOptionsStmt(Table.Id eId) {
                return new SqlStatement.Builder(String.format(
                        "SELECT pg_get_userbyid(relowner), spcname, relhasoids, reloptions\n" +
                                "FROM pg_catalog.pg_class\n" +
                                "LEFT JOIN pg_tablespace ON reltablespace = pg_tablespace.oid\n" +
                                "LEFT JOIN pg_namespace ON relnamespace = pg_namespace.oid\n" +
                                "WHERE relname = '%s' AND nspname = '%s' ", eId.getName(), eId.getSchema()))
                        .build();
            }

            @Override
            public RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<Table>>> getOptionsRowMapper() {
                return new RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<Table>>>() {
                    @Override
                    public PostgreSqlTableOptions.Builder mapRow(ResultSet rs, int i) throws SQLException {
                        PostgreSqlTableOptions.Builder builder = new PostgreSqlTableOptions.Builder()
                                .owner(rs.getString(1))
                                .oids(rs.getBoolean(3));
                        String tablespace = rs.getString(2);
                        if (tablespace == null) tablespace = "pg_default";
                        builder.tableSpace(tablespace);
                        String s = rs.getString(4);
                        if (s != null) {
                            s = s.substring(1, s.length() - 1);
                            String[] storageParameters = s.split(",");
                            for (String option : storageParameters) {
                                int equalPosition = option.indexOf('=');
                                builder.addStorageParameter(option.substring(0, equalPosition).toUpperCase(),
                                        option.substring(equalPosition + 1));
                            }
                        }
                        return builder;
                    }
                };
            }
        };
    }

    @Override
    public AbstractTableMdSqlStatements<Column.Id, Column> getColumnSqlStmtSource() {
        return new AbstractTableMdSqlStatements<Column.Id, Column>() {
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
    public User.Builder getUserBuilder(Id id, String name, UserOptions options) {
        return new User.Builder(id, name, options) {
            @Override
            public User build() {
                id = new User.Id(serverId, name);
                PostgreSqlUserOptions.Builder builder = new PostgreSqlUserOptions.Builder((PostgreSqlUserOptions) options);
                for (Grantee grantee : grantees) {
                    builder.addRole(grantee);
                }
                options = builder.build();
                return new User(this);
            }
        };
    }
}
