package ua.com.nov.model.entity.database;

import ua.com.nov.model.entity.column.Column;

public final class MySqlDb extends Database {

    public MySqlDb(DatabaseID pk) {
        super(pk);
    }

    public MySqlDb(String dbUrl, String userName) {
        super(dbUrl, userName);
    }

    public MySqlDb(String dbUrl, String userName, String password) {
        super(dbUrl, userName, password);
    }

    public MySqlDb(DatabaseID pk, String password) {
        super(pk, password);
    }

    @Override
    public Executable getExecutor() {
        return new MySqlExecutor();
    }

    private class MySqlExecutor extends Executor {
        @Override
        protected void addFullTypeName(Column col, StringBuilder result) {
            result.append(col.getDataType().getTypeName());
            addSizeAndPrecision(col, result);
            if (col.isAutoIncrement()) {
                if (col.getDataType().isAutoIncrement()) result.append(" AUTO_INCREMENT");
                else throw new IllegalArgumentException();
            }
        }
    }
}

