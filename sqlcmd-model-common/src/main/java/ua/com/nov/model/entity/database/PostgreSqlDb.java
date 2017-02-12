package ua.com.nov.model.entity.database;

import ua.com.nov.model.entity.column.Column;

import java.sql.Types;

public class PostgreSqlDb extends Database {

    public PostgreSqlDb(DatabaseID pk) {
        super(pk);
    }

    public PostgreSqlDb(String dbUrl, String userName) {
        super(dbUrl, userName);
    }

    public PostgreSqlDb(String dbUrl, String userName, String password) {
        super(dbUrl, userName, password);
    }

    public PostgreSqlDb(DatabaseID pk, String password) {
        super(pk, password);
    }

    @Override
    public Executable getExecutor() {
        return new PostgreSqlExecutor();
    }

    private class PostgreSqlExecutor extends Executor {
        @Override
        public String getSelectAllDbStmt() {
            return "SELECT datname FROM pg_database WHERE datistemplate = false";
        }

        @Override
        protected void addFullTypeName(Column col, StringBuilder result) {
            if (col.isAutoIncrement()) {
                switch (col.getDataType()) {
                    case Types.SMALLINT:
                        result.append("SMALLSERIAL");
                        break;
                    case Types.INTEGER:
                        result.append("SERIAL");
                        break;
                    case Types.BIGINT:
                        result.append("BIGSERIAL");
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            } else {
                result.append(col.getTypeName());
            }
            addSizeAndPrecision(col, result);
        }
    }

    public enum PostgreSqlDataTypes {}

}
