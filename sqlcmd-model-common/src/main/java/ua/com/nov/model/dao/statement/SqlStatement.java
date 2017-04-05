package ua.com.nov.model.dao.statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlStatement {
    private final String sql;
    private final List<Object> parameters;

    public static class Builder {
        private final String sql;
        private final List<Object> parameters = new ArrayList<>();

        public Builder(String sql) {
            this.sql = sql;
        }

        public Builder(String sql, Object... params) {
            this(sql);
            for (Object param : params) {
                addParameter(param);
            }
        }

        public Builder addParameter(Object param) {
            parameters.add(param);
            return this;
        }

        public SqlStatement build() {
            return new SqlStatement(this);
        }
    }

    public SqlStatement(Builder builder) {
        this.sql = builder.sql;
        this.parameters = builder.parameters;
    }

    public String getSql() {
        return sql;
    }

    public List<Object> getParameters() {
        return Collections.unmodifiableList(parameters);
    }
}
