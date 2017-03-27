package ua.com.nov.model.statement;

import ua.com.nov.model.entity.metadata.database.Database;

public abstract class AbstractDbSqlStatements extends BaseSqlStmtSource<Database.Id, Database, Database> {
    public static final String CREATE_DB_SQL = "CREATE %s";
    public static final String DROP_DB_SQL = "DROP %s %s";


    @Override
    public String getCreateStmt(Database db) {
        return String.format(CREATE_DB_SQL, db.toString());
    }

    @Override
    public String getDeleteStmt(Database.Id id) {
        return String.format(DROP_DB_SQL, id.getMdName(), id.getFullName());
    }
}