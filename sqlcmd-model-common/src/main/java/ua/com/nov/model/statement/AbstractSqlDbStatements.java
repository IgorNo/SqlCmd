package ua.com.nov.model.statement;

import ua.com.nov.model.entity.metadata.database.Database;

public abstract class AbstractSqlDbStatements extends BaseSqlStmtSource<Database.DbId, Database, Database> {
    public static final String CREATE_DB_SQL = "CREATE DATABASE %s %s";
    public static final String DROP_DB_SQL = "DROP DATABASE %s";


    @Override
    public String getCreateStmt(Database db) {
        return String.format(CREATE_DB_SQL, db.getName(), db.getDbProperties());
    }

    @Override
    public String getDeleteStmt(Database.DbId dbId) {
        return String.format(DROP_DB_SQL, dbId.getName());
    }
}