package ua.com.nov.model.entity.metadata.server;

import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.dao.statement.AbstractDatabaseMdSqlStatements;
import ua.com.nov.model.dao.statement.AbstractTableMdSqlStatements;
import ua.com.nov.model.dao.statement.OptionsSqlStmtSource;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.entity.MetaDataId;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.AbstractMetaData;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.database.MySqlDbOptions;
import ua.com.nov.model.entity.metadata.datatype.DataTypes;
import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.grantee.privelege.MySqlPrivilege;
import ua.com.nov.model.entity.metadata.grantee.privelege.Privilege;
import ua.com.nov.model.entity.metadata.grantee.user.MySqlUserOptions;
import ua.com.nov.model.entity.metadata.grantee.user.User;
import ua.com.nov.model.entity.metadata.grantee.user.UserOptions;
import ua.com.nov.model.entity.metadata.schema.Schema;
import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.MySqlTableOptions;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.column.ColumnOptions;
import ua.com.nov.model.entity.metadata.table.column.MySqlColumnOptions;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.com.nov.model.entity.metadata.grantee.user.MySqlUserOptions.ResourceOption.*;

public final class MysqlServer extends Server {

    public MysqlServer(String dbUrl) {
        super(dbUrl);
        initTypesMap();
    }

    private void initTypesMap() {
        getTypesMap().put(DataTypes.INTEGER, "INT");
        getTypesMap().put(DataTypes.LONGVARCHAR, "TEXT");
        getTypesMap().put(DataTypes.NUMERIC, "DECIMAL");
    }

    @Override
    public ColumnOptions.Builder<MySqlColumnOptions> createColumnOptions() {
        return new MySqlColumnOptions.Builder();
    }

    @Override
    public String convert(String parameter) {
        if (parameter != null) return parameter.toLowerCase();
        return parameter;
    }

    @Override
    public AbstractDatabaseMdSqlStatements getDatabaseSqlStmtSource() {
        return new AbstractDatabaseMdSqlStatements<Database.Id, Database, Id>() {
            @Override
            public SqlStatement getReadAllStmt(Id cId) {
                return new SqlStatement.Builder("SHOW DATABASES").build();
            }

            @Override
            public SqlStatement getRenameStmt(Database eId, String newName) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected String getCommentStmt(Database entity) {
                return "";
            }
        };
    }

    @Override
    protected OptionsSqlStmtSource<Database.Id, Database> getDatabaseOptionsSqlStmtSource() {
        return new OptionsSqlStmtSource<Database.Id, Database>() {
            @Override
            public SqlStatement getReadOptionsStmt(Database.Id eId) {
                return new SqlStatement.Builder(
                        "SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME  " +
                                "FROM information_schema.SCHEMATA " +
                                "WHERE SCHEMA_NAME = '" + eId.getName() + "'").build();
            }

            @Override
            public RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<Database>>> getOptionsRowMapper() {
                return new RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<Database>>>() {
                    @Override
                    public MetaDataOptions.Builder<? extends MetaDataOptions<Database>> mapRow(ResultSet rs, int i)
                            throws SQLException {

                        return new MySqlDbOptions.Builder()
                                .characterSet(rs.getString(1)).collate(rs.getString(2));                    }
                };
            }

        };
    }

    @Override
    public <I extends AbstractMetaData.Id<C>, E extends AbstractMetaData<I>, C extends MetaDataId>
    AbstractDatabaseMdSqlStatements<I, E, C> getDatabaseMdSqlStmtSource() {
        return new AbstractDatabaseMdSqlStatements<I, E, C>() {
            @Override
            public SqlStatement getDeleteStmt(I eId) {
                if (eId.getClass() == Index.Id.class)
                    return new SqlStatement.Builder(String.format("ALTER TABLE %s DROP INDEX %s",
                            eId.getContainerId().getFullName(), eId.getName())).build();
                else
                    return super.getDeleteStmt(eId);
            }

        };
    }

    @Override
    public OptionsSqlStmtSource<Grantee.Id, User> getUserOptionsSqlStmSource() {
        return new OptionsSqlStmtSource<Grantee.Id, User>() {
            private String sql =
                    "SELECT User, Host, authentication_string, " +
                            "max_questions, max_updates, max_connections, max_user_connections, account_locked\n" +
                            "FROM mysql.user\n";
            @Override
            public SqlStatement getReadAllOptionsStmt() {
                return new SqlStatement.Builder(sql).build();
            }

            @Override
            public SqlStatement getReadOptionsStmt(Grantee.Id eId) {
                int pos = eId.getName().indexOf('@');
                StringBuilder sb = new StringBuilder("WHERE User = ").append(eId.getName().substring(0, pos))
                        .append(" AND Host = ").append(eId.getName().substring(pos + 1));
                return new SqlStatement.Builder(sql + sb.toString()).build();
            }

            @Override
            public RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<User>>> getOptionsRowMapper() {
                return new RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<User>>>() {
                    @Override
                    public MySqlUserOptions.Builder mapRow(ResultSet rs, int i) throws SQLException {
                        MySqlUserOptions.Builder builder = new MySqlUserOptions.Builder()
                                .host(rs.getString("Host"))
                                .password(rs.getString("authentication_string"))
                                .addResourceOption(MAX_CONNECTIONS_PER_HOUR, rs.getInt("max_connections"))
                                .addResourceOption(MAX_UPDATES_PER_HOUR, rs.getInt("max_updates"))
                                .addResourceOption(MAX_QUERIES_PER_HOUR, rs.getInt("max_questions"))
                                .addResourceOption(MAX_USER_CONNECTIONS, rs.getInt("max_user_connections"));
                        if (rs.getString("account_locked").equals("Y"))
                            builder.lockOption(MySqlUserOptions.LockOption.LOCK);
                        else
                            builder.lockOption(MySqlUserOptions.LockOption.UNLOCK);
                        return builder;
                    }
                };
            }
        };
    }

    @Override
    public AbstractDatabaseMdSqlStatements<Table.Id, Table, Schema.Id> getTableSqlStmtSource() {
        return new AbstractDatabaseMdSqlStatements<Table.Id, Table, Schema.Id>() {
            @Override
            protected String getCommentStmt(Table table) {
                if (table.getViewName() == null) return "";
                return String.format("\nALTER TABLE %s COMMENT '%s';", table.getId().getFullName(), table.getViewName());
            }
        };
    }

    @Override
    public <I extends TableMd.Id, E extends TableMd> AbstractTableMdSqlStatements<I, E> getTableMdSqlStmtSource() {
        return new AbstractTableMdSqlStatements<I, E>() {
            @Override
            public SqlStatement getDeleteStmt(I id) {
                if (id.getClass().equals(UniqueKey.Id.class))
                    return new SqlStatement.Builder(String.format("ALTER TABLE %s DROP KEY %s",
                            id.getTableId().getFullName(), id.getName())).build();
                if (id.getClass().equals(ForeignKey.Id.class))
                    return new SqlStatement.Builder(String.format("ALTER TABLE %s DROP FOREIGN KEY %s",
                            id.getTableId().getFullName(), id.getName())).build();
                if (id.getClass().equals(PrimaryKey.Id.class))
                    return new SqlStatement.Builder(String.format("ALTER TABLE %s DROP PRIMARY KEY",
                            id.getTableId().getFullName())).build();
                throw new UnsupportedOperationException();
            }

            @Override
            public SqlStatement getRenameStmt(E entity, String newName) {
                if (entity.getClass() == UniqueKey.class)
                    return new SqlStatement.Builder(String.format("ALTER TABLE %s RENAME KEY %s TO %s",
                            entity.getTableId().getFullName(), entity.getName(), newName)).build();
                else
                    throw new UnsupportedOperationException();
            }
        };

    }

    @Override
    protected OptionsSqlStmtSource<Table.Id, Table> getTableOptionsSqlStmtSource() {
        return new OptionsSqlStmtSource<Table.Id, Table>() {
            @Override
            public SqlStatement getReadOptionsStmt(Table.Id eId) {
                return new SqlStatement.Builder(
                        "SELECT ENGINE, AVG_ROW_LENGTH, AUTO_INCREMENT, " +
                                "TABLE_COLLATION, CHECKSUM, TABLE_COMMENT, CREATE_OPTIONS, ROW_FORMAT \n" +
                                "FROM information_schema.TABLES \n" +
                                "WHERE TABLE_SCHEMA = '" + eId.getCatalog() + "' AND TABLE_NAME = '" + eId.getName() + "'")
                        .build();
            }


            @Override
            public RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<Table>>> getOptionsRowMapper() {
                return new RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<Table>>>() {
                    @Override
                    public MySqlTableOptions.Builder mapRow(ResultSet rs, int i) throws SQLException {
                        String collate = rs.getString(4);
                        MySqlTableOptions.Builder builder = new MySqlTableOptions.Builder()
                                .engine(rs.getString(1)).avgRowLength(rs.getInt(2))
                                .autoIncrement(rs.getInt(3)).collate(collate)
                                .checkSum(rs.getInt(5) == 1 ? true : false)
                                .comment(rs.getString(6)).rowFormat(rs.getString(8));
                        builder.defaultCharset(collate.substring(0, collate.indexOf('_')));
                        String[] createOptions = rs.getString(7).split(" ");
                        if (!createOptions[0].isEmpty()) {
                            for (String option : createOptions) {
                                int equalPosition = option.indexOf('=');
                                builder.addOption(option.substring(0, equalPosition).toUpperCase(),
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
            public SqlStatement getRenameStmt(Column col, String newName) {
                Column.Builder builder = new Column.Builder(col);
                builder.setName(newName);
                return new SqlStatement.Builder(String.format("ALTER TABLE %s CHANGE COLUMN %s %s;",
                        col.getTableId().getFullName(), col.getName(), builder.build().getCreateStmtDefinition(null)))
                        .build();
            }

            @Override
            public SqlStatement getUpdateStmt(Column col) {
                return new SqlStatement.Builder(String.format("ALTER TABLE %s MODIFY COLUMN %s;",
                        col.getTableId().getFullName(), col.getCreateStmtDefinition(null)))
                        .build();
            }

            @Override
            public String getCommentStmt(Column column) {
                if (column.getViewName() == null) return "";
                return "/n" + getUpdateStmt(column).getSql();
            }
        };
    }

    @Override
    protected OptionsSqlStmtSource<Column.Id, Column> getColumnOptionsSqlStmSource() {
        return new OptionsSqlStmtSource<Column.Id, Column>() {
            @Override
            public SqlStatement getReadOptionsStmt(Column.Id eId) {
                return new SqlStatement.Builder(
                        "SELECT CHARACTER_SET_NAME, COLLATION_NAME, COLUMN_TYPE, EXTRA, " +
                                "GENERATION_EXPRESSION, COLUMN_COMMENT \n" +
                                "FROM information_schema.COLUMNS \n" +
                                "WHERE TABLE_SCHEMA = '" + eId.getCatalog() +
                                "' AND TABLE_NAME = '" + eId.getTableId().getName() +
                                "' AND COLUMN_NAME = '" + eId.getName() + "'")
                        .build();
            }

            @Override
            public RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<Column>>> getOptionsRowMapper() {
                return new RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<Column>>>() {
                    @Override
                    public MetaDataOptions.Builder<? extends MetaDataOptions<Column>> mapRow(ResultSet rs, int i)
                            throws SQLException {
                        String collName = rs.getString(2);
                        MySqlColumnOptions.Builder builder = new MySqlColumnOptions.Builder()
                                .charSet(rs.getString(1)).collation(collName)
                                .comment(rs.getString(6));

                        if (collName != null && collName.contains("_bin")) {
                            builder.binari();
                        }
                        String colType = rs.getString(3);
                        if (colType != null && !colType.isEmpty()) {
                            if (colType.contains("unsigned")) builder.unsigned();
                            if (colType.contains("zerofill")) builder.zeroFill();
                        }
                        String genExpr = rs.getString(5);
                        if (genExpr != null && !genExpr.isEmpty()) {
                            String extra = rs.getString(4);
                            switch (extra) {
                                case "VIRTUAL GENERATED":
                                    builder.generationExpression(genExpr, MySqlColumnOptions.GenerationColumnType.VIRTUAL);
                                    break;
                                case "STORED GENERATED":
                                    builder.generationExpression(genExpr, MySqlColumnOptions.GenerationColumnType.STORAGE);
                            }
                        }

                        return builder;
                    }
                };
            }
        };
    }

    @Override
    public User.Builder getUserBuilder(Id id, String name, UserOptions options) {
        return new User.Builder(id, name, options) {
            @Override
            public User build() {
                StringBuilder sb = new StringBuilder();
                if (!name.contains("@")) {
                    sb.append("'").append(name).append("'@'");
                    if (options == null) {
                        options = new MySqlUserOptions.Builder().host("%").build();
                    }
                    sb.append(((MySqlUserOptions) options).getHost()).append("'");
                } else {
                    sb.append(name);
                }
                id = new User.Id(serverId, sb.toString());
                for (Grantee grantee : grantees) {
                    for (Privilege privilege : grantee.getAllPrivileges()) {
                        if (privilege.isWithGrantOptions())
                            addPrivelege(new MySqlPrivilege.Builder((MySqlPrivilege) privilege));
                    }
                }
                return new User(this);
            }
        };
    }

}

