package ua.com.nov.model.entity.metadata.database;

import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.dao.statement.AbstractDatabaseMdSqlStatements;
import ua.com.nov.model.dao.statement.AbstractTableMdSqlStatements;
import ua.com.nov.model.dao.statement.OptionsSqlStmtSource;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.entity.Hierarchical;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.MetaData;
import ua.com.nov.model.entity.metadata.MetaDataId;
import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.schema.Schema;
import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class MySqlDb extends Database {

    public MySqlDb(String dbUrl, String dbName, MetaDataOptions<MySqlDb> options) {
        super(dbUrl, dbName, options);
        initTypesMap();
    }

    public MySqlDb(String dbUrl, String dbName) {
        this(dbUrl, dbName, null);
    }

    private void initTypesMap() {
        getTypesMap().put(JdbcDataTypes.INTEGER, "INT");
        getTypesMap().put(JdbcDataTypes.LONGVARCHAR, "TEXT");
        getTypesMap().put(JdbcDataTypes.NUMERIC, "DECIMAL");
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
        return new AbstractDatabaseMdSqlStatements<Id, MySqlDb, Database>() {
            @Override
            public SqlStatement getReadAllStmt(Database cId) {
                return new SqlStatement.Builder("SHOW DATABASES").build();
            }

            @Override
            public SqlStatement getRenameStmt(MySqlDb eId, String newName) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected String getCommentStmt(MySqlDb entity) {
                return "";
            }
        };
    }

    @Override
    protected OptionsSqlStmtSource<Id, MySqlDb> getDatabaseOptionsSqlStmtSource() {
        return new OptionsSqlStmtSource<Id, MySqlDb>() {
            @Override
            public SqlStatement getReadOptionsStmt(Id eId) {
                return new SqlStatement.Builder(
                        "SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME  " +
                                "FROM information_schema.SCHEMATA " +
                                "WHERE SCHEMA_NAME = '" + eId.getName() + "'").build();
            }


            @Override
            public RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<MySqlDb>>> getOptionsRowMapper() {
                return new RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<MySqlDb>>>() {
                    @Override
                    public Options.Builder mapRow(ResultSet rs, int i) throws SQLException {
                        return new MySqlDb.Options.Builder()
                                .characterSet(rs.getString(1)).collate(rs.getString(2));
                    }
                };
            }

        };
    }

    @Override
    public <I extends MetaDataId<C>, E extends MetaData<I>, C extends Hierarchical>
    AbstractDatabaseMdSqlStatements<I, E, C> getDatabaseMdSqlStmtSource() {
        return new AbstractDatabaseMdSqlStatements<I, E, C>() {
            @Override
            public SqlStatement getDeleteStmt(E entity) {
                if (entity.getClass() == Index.class)
                    return new SqlStatement.Builder(String.format("ALTER TABLE %s DROP INDEX %s",
                            entity.getId().getContainerId().getFullName(), entity.getName())).build();
                else
                    return super.getDeleteStmt(entity);
            }

        };
    }

    @Override
    public AbstractDatabaseMdSqlStatements<Table.Id, Table, Schema.Id> getTableSqlStmtSource() {
        return new AbstractDatabaseMdSqlStatements<Table.Id, Table, Schema.Id>() {
            @Override
            protected String getCommentStmt(Table table) {
                if (table.getViewName() == null) return "";
                return String.format("\nALTER TABLE %s COMMENT '%s'", table.getId().getFullName(), table.getViewName());
            }
        };
    }

    @Override
    public <I extends TableMd.Id, E extends TableMd> AbstractTableMdSqlStatements<I, E> getTableMdSqlStmtSource() {
        return new AbstractTableMdSqlStatements<I, E>() {
            @Override
            public SqlStatement getDeleteStmt(TableMd entity) {
                if (entity.getClass() == UniqueKey.class)
                    return new SqlStatement.Builder(String.format("ALTER TABLE %s DROP KEY %s",
                            entity.getTableId().getFullName(), entity.getName())).build();
                if (entity.getClass() == ForeignKey.class)
                    return new SqlStatement.Builder(String.format("ALTER TABLE %s DROP FOREIGN KEY %s",
                            entity.getTableId().getFullName(), entity.getName())).build();
                if (entity.getClass() == PrimaryKey.class)
                    return new SqlStatement.Builder(String.format("ALTER TABLE %s DROP %s",
                            entity.getTableId().getFullName(), entity.getType())).build();
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
                return new SqlStatement.Builder(String.format("ALTER TABLE %s CHANGE COLUMN %s %s",
                        col.getTableId().getFullName(), col.getName(), builder.build().getCreateStmtDefinition(null)))
                        .build();
            }

            @Override
            public SqlStatement getUpdateStmt(Column col) {
                return new SqlStatement.Builder(String.format("ALTER TABLE %s MODIFY COLUMN %s",
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

    public static class Options extends MetaDataOptions<MySqlDb> {
        public Options(Builder builder) {
            super(builder);
        }

        public String getCharacterSet() {
            return getOption("CHARACTER SET");
        }

        public String getCollate() {
            return getOption("COLLATE");
        }

        public static class Builder extends MetaDataOptions.Builder<Options> {
            public Builder() {
                super(MySqlDb.class);
            }

            public Builder characterSet(String characterSet) {
                addOption("CHARACTER SET", characterSet);
                return this;
            }

            public Builder collate(String collate) {
                addOption("COLLATE", collate);
                return this;
            }

            @Override
            public Options build() {
                return new Options(this);
            }
        }

    }
}

