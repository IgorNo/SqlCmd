package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabaseID;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class DatabaseDao extends AbstractDao<DatabaseID, Database, Object> {

    protected DatabaseDao() {
    }

    @Override
    public void create(Database db) throws SQLException {
        Statement stmt = getDataSource().getConnection().createStatement();
        stmt.executeUpdate(db.getExecutor().getCreateDbStmt());
        stmt.close();
    }

    @Override
    public void delete(Database db) throws SQLException {
        Statement stmt = getDataSource().getConnection().createStatement();
        stmt.executeUpdate(db.getExecutor().getDropDbStmt());
        stmt.close();
    }

    @Override
    public Map<DatabaseID, Database> readAllFrom(Object ambient) throws SQLException {
        return readAll();
    }

    @Override
    public int count(Object ambient) throws SQLException {
        return readAllFrom(ambient).size();
    }
}
