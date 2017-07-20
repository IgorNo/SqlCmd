package ua.com.nov.model.dao.statement;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlParameterValue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SqlStatement {
    private final String sql;
    private final List<SqlParameterValue> parameters;

    private SqlStatement(Builder builder) {
        this.sql = builder.sql;
        this.parameters = builder.parameters;
    }

    public String getSql() {
        return sql;
    }

    public List<SqlParameterValue> getSqlParameterValues() {
        return Collections.unmodifiableList(parameters);
    }

    public List<SqlParameter> getSqlParameters() {
        List<SqlParameter> result = new LinkedList<>();
        for (SqlParameterValue parameterValue : parameters) {
            result.add(new SqlParameter(parameterValue));
        }
        return Collections.unmodifiableList(parameters);
    }

    public Object[] getValues() {
        Object[] result = new Object[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            result[i] = parameters.get(i).getValue();
        }
        return result;
    }

    public int[] getValueTypes() {
        int[] result = new int[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            result[i] = parameters.get(i).getSqlType();
        }
        return result;
    }

    @Override
    public String toString() {
        return sql;
    }

    public static class Builder {
        private final String sql;
        private final List<SqlParameterValue> parameters = new LinkedList<>();

        public Builder(String sql) {
            this.sql = sql;
        }

        public Builder(String sql, SqlParameterValue... params) {
            this(sql);
            for (SqlParameterValue param : params) {
                addParameter(param);
            }
        }

        public Builder addParameter(SqlParameterValue param) {
            parameters.add(param);
            return this;
        }

        public SqlStatement build() {
            return new SqlStatement(this);
        }
    }
}
