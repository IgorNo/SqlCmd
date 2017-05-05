package ua.com.nov.model.entity.metadata.database;

import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.dao.statement.AbstractColumnSqlStatements;
import ua.com.nov.model.dao.statement.AbstractMetaDataSqlStatements;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.schema.Schema;
import ua.com.nov.model.entity.metadata.table.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PostgresSqlDb extends Database {

    public PostgresSqlDb(String dbUrl, String dbName, MetaDataOptions<PostgresSqlDb> options) {
        super(dbUrl, dbName, options);
        getTypesMap().put(JdbcDataTypes.LONGVARCHAR, "text");
    }

    public PostgresSqlDb(String dbUrl, String dbName) {
        this(dbUrl, dbName, null);
    }

    @Override
    public String getAutoIncrementDefinition() {
        return "";
    }

    @Override
    public String convert(String parameter) {
        if (parameter != null) return parameter.toLowerCase();
        return parameter;
    }

    @Override
    public String[] getTableTypes() {
        List<String> result = new LinkedList<>();
        for (String type : tableTypes) {
            if (type.toUpperCase().contains("TABLE") || type.toUpperCase().contains("VIEW"))
                result.add(type);
        }
        return result.toArray(new String[0]);
    }

    @Override
    public AbstractMetaDataSqlStatements getDatabaseSqlStmtSource() {
        return new AbstractMetaDataSqlStatements<Database.Id, PostgresSqlDb, Database>() {
            @Override
            public SqlStatement getReadAllStmt(Database cId) {
                return new SqlStatement.Builder("SELECT datname FROM pg_database WHERE datistemplate = false")
                        .build();
            }

            @Override
            public SqlStatement getReadOptionsStmt(Id eId) {
                return new SqlStatement.Builder(
                        "SELECT pg_encoding_to_char(encoding), datcollate, datctype, datistemplate, datallowconn, " +
                                "datconnlimit, rolname, spcname\n" +
                                "FROM pg_catalog.pg_database\n" +
                                "LEFT JOIN pg_catalog.pg_authid ON datdba = pg_catalog.pg_authid.oid\n" +
                                "LEFT JOIN pg_catalog.pg_tablespace ON dattablespace = pg_catalog.pg_tablespace.oid\n" +
                                "WHERE datname = " + "'" + eId.getName() + "'").build();
            }

            @Override
            public RowMapper<Optional<PostgresSqlDb>> getOptionsRowMapper() {
                return new RowMapper<Optional<PostgresSqlDb>>() {
                    @Override
                    public Optional<PostgresSqlDb> mapRow(ResultSet rs, int i) throws SQLException {
                        return new PostgresSqlDb.Options.Builder()
                                .encoding(rs.getString(1)).lcCollate(rs.getString(2))
                                .lcType(rs.getString(3)).isTemplate(rs.getBoolean(4))
                                .allowConn(rs.getBoolean(5)).connLimit(rs.getInt(6))
                                .owner(rs.getString(7)).tableSpace(rs.getString(8))
                                .build();
                    }
                };
            }
        };
    }

    @Override
    public AbstractMetaDataSqlStatements<Table.Id, Table, Schema.Id> getTableSqlStmtSource() {
        return new AbstractMetaDataSqlStatements<Table.Id, Table, Schema.Id>() {
            @Override
            public SqlStatement getReadOptionsStmt(Table.Id eId) {
                return new SqlStatement.Builder(String.format(
                        "SELECT pg_get_userbyid(c.relowner), spcname, relhasoids, reloptions\n" +
                                "FROM pg_catalog.pg_class\n" +
                                "LEFT JOIN pg_tablespace ON reltablespace = pg_tablespace.oid\n" +
                                "LEFT JOIN pg_namespace ON relnamespace = pg_namespace.oid\n" +
                                "WHERE relname = '%s' AND relnamespace = '%s' ", eId.getName(), eId.getSchema()))
                        .build();
            }

            @Override
            public RowMapper<Optional<Table>> getOptionsRowMapper() {
                return new RowMapper<Optional<Table>>() {
                    @Override
                    public Optional<Table> mapRow(ResultSet rs, int i) throws SQLException {
                        return new PostgresSqlTableOptions.Builder()
                                .owner(rs.getString(1)).tableSpace(rs.getString(2))
                                .oids(rs.getBoolean(3)).storageParameters(rs.getString(4))
                                .build();
                    }
                };
            }
        };
    }

    @Override
    public AbstractColumnSqlStatements getColumnSqlStmtSource() {
        return new AbstractColumnSqlStatements() {
//            @Override
//            public SqlStatement getUpdateStmt(Column col) {
//                return new SqlStatement.Builder("ALTER TABLE %s RENAME COLUMN %s TO %s",
//                        col.getTableId().getFullName(), col.getName(), col.getNewName()).build();
//            }
        };
    }

    public static class Options extends MetaDataOptions<PostgresSqlDb> {

        public static class Builder extends MetaDataOptions.Builder<Options> {

            public Builder() {
                super(PostgresSqlDb.class);
            }

            public Builder owner(String owner) {
                addOption("OWNER", owner);
                return this;
            }

            public Builder tableSpace(String tableSpace) {
                addOption("TABLESPACE", tableSpace);
                return this;
            }

            public Builder allowConn(Boolean allowConn) {
                addOption("ALLOW_CONNECTIONS", allowConn.toString());
                return this;
            }

            public Builder connLimit(Integer connLimit) {
                addOption("CONNECTION LIMIT", connLimit.toString());
                return this;
            }

            public Builder isTemplate(Boolean isTemplate) {
                addOption("IS_TEMPLATE", isTemplate.toString());
                return this;
            }

            public Builder encoding(String encoding) {
                addOption("ENCODING", "'" + encoding + "'");
                return this;
            }

            public Builder lcCollate(String lcCollate) {
                addOption("LC_COLLATE", "'"  + lcCollate + "'");
                return this;
            }

            public Builder lcType(String lcType) {
                addOption("LC_CTYPE", "'" + lcType + "'");
                return this;
            }

            @Override
            public Options build() {
                return new Options(this);
            }
        }

        public Options(Builder builder) {
            super(builder);
        }

        @Override
        public List<String> getUpdateOptionsDefinition() {
            final StringBuilder sb = new StringBuilder("");
            if (getAllowConn() != null) sb.append("\n\tALLOW_CONNECTIONS ").append(getAllowConn());
            if (getConnLimit() != null) sb.append("\n\tCONNECTION LIMIT ").append(getConnLimit());
            if (isTemplate() != null) sb.append("\n\tIS_TEMPLATE ").append(isTemplate());

            List<String> result = new LinkedList<>();
            result.add(sb.toString());

            if (getOwner() != null) result.add("OWNER TO " + getOwner());
            if (getTableSpace() != null) result.add("SET TABLESPACE " + getTableSpace());

            return result;
        }

        public String getOwner() {
            return getOption("OWNER");
        }

        public String getTableSpace() {
            return getOption("TABLESPACE");
        }

        public Boolean getAllowConn() {
            return Boolean.valueOf(getOption("ALLOW_CONNECTIONS"));
        }

        public Integer getConnLimit() {
            return Integer.valueOf(getOption("CONNECTION LIMIT"));
        }

        public Boolean isTemplate() {
            return Boolean.valueOf(getOption("IS_TEMPLATE"));
        }

        public String getEncoding() {
            return getOption("ENCODING");
        }

        public String getLcCollate() {
            return getOption("LC_COLLATE");
        }

        public String getLcType() {
            return getOption("LC_CTYPE");
        }
    }

}
