package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.entity.Buildable;
import ua.com.nov.model.entity.MdCreateOptions;
import ua.com.nov.model.entity.MdUpdateOptions;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.constraint.Constraint;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;
import ua.com.nov.model.statement.*;

import java.util.*;

public class PostgresSqlDb extends Database {

    public PostgresSqlDb(String dbUrl, String dbName) {
        this(dbUrl, dbName, null);
        getTypesMap().put(JdbcDataTypes.LONGVARCHAR, "text");
    }

    public PostgresSqlDb(String dbUrl, String dbName, Options dbProperties) {
        super(dbUrl, dbName, dbProperties);
        getTypesMap().put(JdbcDataTypes.LONGVARCHAR, "text");
    }

    @Override
    public String getAutoIncrementDefinition() {
        return "";
    }

    @Override
    public String getFullTableName(Table.Id id) {
        StringBuilder result = new StringBuilder();
        if (id.getSchema() != null) result.append(id.getSchema()).append('.');
        return result.append(id.getName()).toString();
    }

    @Override
    public String convert(String parameter) {
        if (parameter != null) return parameter.toLowerCase();
        return parameter;
    }

    @Override
    public AbstractMetaDataSqlStatements getDatabaseSqlStmtSource() {
        return new AbstractMetaDataSqlStatements() {
            @Override
            public SqlStatement getReadAllStmt(Object db) {
                return new SqlStatement.Builder("SELECT datname FROM pg_database WHERE datistemplate = false")
                        .build();
            }
        };
    }

    @Override
    public AbstractTableSqlStatements getTableSqlStmtSource() {
        return new AbstractTableSqlStatements() { };
    }

    @Override
    public AbstractColumnSqlStatements getColumnSqlStmtSource() {
        return new AbstractColumnSqlStatements() {
            @Override
            public SqlStatement getUpdateStmt(Column col) {
                return new SqlStatement.Builder("ALTER TABLE %s RENAME COLUMN %s TO %s",
                        col.getTableId().getFullName(), col.getName(), col.getNewName()).build();
            }
        };
    }

    @Override
    public AbstractConstraintSqlStatements<PrimaryKey> getPrimaryKeySqlStmtSource() {
        return new ConstraintSqlStatements<>();
    }

    private static class ConstraintSqlStatements<V extends Constraint> extends AbstractConstraintSqlStatements<V> {
        @Override
        public SqlStatement getUpdateStmt(V value) {
            return new SqlStatement.Builder("ALTER TABLE %s RENAME CONSTRAINT %s TO %s",
                    value.getTableId().getFullName(), value.getName(), value.getNewName()).build();
        }
    }

    @Override
    public AbstractConstraintSqlStatements<ForeignKey> getForeignKeySqlStmtSource() {
        return new ConstraintSqlStatements<>();
    }

    @Override
    public AbstractConstraintSqlStatements<UniqueKey> getUniqueKeySqlStmtSource() {
        return new ConstraintSqlStatements<>();
    }

    @Override
    public AbstractIndexSqlStatements getIndexSqlStmtSource() {
        return new AbstractIndexSqlStatements() {};
    }

    private abstract static class Options implements MetaDataOptions {
        protected final List<String> optionList = new LinkedList<>();

        @Override
        public List<String> getOptionList() {
            return Collections.unmodifiableList(optionList);
        }

        protected abstract static class Builder implements Buildable<Options> {
            String owner;
            String tableSpace;
            Boolean allowConn;
            Integer connLimit;
            Boolean isTemplate;


            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("");
                if (allowConn != null)sb.append("\n\tALLOW_CONNECTIONS = ").append(allowConn);
                if (connLimit != null) sb.append("\n\tCONNECTION LIMIT = ").append(connLimit);
                if (isTemplate != null) sb.append("\n\tIS_TEMPLATE = ").append(isTemplate);
                return sb.toString();
            }

        }
    }

    public static final class CreateOptions extends Options implements MdCreateOptions {

        public static final class Builder extends Options.Builder {
            private String encoding;
            private String lcCollate;
            private String lcType;

            public Builder owner(String owner) {
                this.owner = owner;
                return this;
            }

            public Builder tableSpace(String tableSpace) {
                this.tableSpace = tableSpace;
                return this;
            }

            public Builder allowConn(boolean allowConn) {
                this.allowConn = allowConn;
                return this;
            }

            public Builder connLimit(int connLimit) {
                this.connLimit = connLimit;
                return this;
            }

            public Builder isTemplate(boolean isTemplate) {
                this.isTemplate = isTemplate;
                return this;
            }

            public Builder encoding(String encoding) {
                this.encoding = encoding;
                return this;
            }

            public Builder lcCollate(String lcCollate) {
                this.lcCollate = lcCollate;
                return this;
            }

            public Builder lcType(String lcType) {
                this.lcType = lcType;
                return this;
            }

            @Override
            public CreateOptions build() {
                return new CreateOptions(this);
            }

            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("\n\tWITH");

                if (owner != null) sb.append("\n\tOWNER = ").append(owner);
                if (encoding != null) sb.append("\n\tENCODING  = ").append(encoding);
                if (tableSpace != null) sb.append("\n\tTABLESPACE = ").append(tableSpace);
                if (lcCollate != null) sb.append("\n\tLC_COLLATE  = '").append(lcCollate).append("'");
                if (lcType != null) sb.append("\n\tLC_CTYPE  = '").append(lcType).append("'");

                return sb.append(super.toString()).toString();
            }
        }

        public CreateOptions(Builder builder) {
            this.optionList.add(builder.toString());
        }

        @Override
        public String toString() {
            return optionList.get(0);
        }
    }

    public static final class UpdateOptions extends Options implements MdUpdateOptions {

        public static class Builder extends Options.Builder {
            private String newName;
            private Map<String, String> setParameters = new HashMap<>();
            private Set<String> resetParameters = new HashSet<>();

            public Builder owner(String owner) {
                this.owner = owner;
                return this;
            }

            public Builder tableSpace(String tableSpace) {
                this.tableSpace = tableSpace;
                return this;
            }

            public Builder allowConn(boolean allowConn) {
                this.allowConn = allowConn;
                return this;
            }

            public Builder connLimit(int connLimit) {
                this.connLimit = connLimit;
                return this;
            }

            public Builder isTemplate(boolean isTemplate) {
                this.isTemplate = isTemplate;
                return this;
            }


            public Builder newName(String newName) {
                this.newName = newName;
                return this;
            }

            public Builder addSetParameter(String parameterName, String parameterValue ) {
                setParameters.put(parameterName, parameterValue);
                return this;
            }

            public Builder addResetParameter(String parameterName) {
                resetParameters.add(parameterName);
                return this;
            }

            @Override
            public UpdateOptions build() {
                return new UpdateOptions(this);
            }
        }

        public UpdateOptions(Builder builder) {
            if (!builder.toString().isEmpty()) optionList.add(builder.toString());
            if (builder.owner != null) optionList.add("OWNER TO " + builder.owner);
            if (builder.tableSpace != null) optionList.add("SET TABLESPACE " + builder.tableSpace);
            for (Map.Entry<String, String> entry : builder.setParameters.entrySet()) {
                optionList.add("SET " + entry.getKey() + " TO " + entry.getValue());
            }
            for (String parameter : builder.resetParameters) {
                optionList.add("RESET " + parameter);
            }
            if (builder.newName != null) optionList.add("RENAME TO " + builder.newName);
        }
    }
}
