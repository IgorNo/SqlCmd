package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabasePK;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class DatabaseDao extends AbstractDao<DatabasePK, Database, Object> {

    protected DatabaseDao() {
    }

    @Override
    public void create(Database db) throws SQLException {
        Statement stmt = getDataSource().getConnection().createStatement();
        stmt.executeUpdate(String.format(db.getExecutor().getCreateDbStmt(), db.getName(), db.getProperties()));
        stmt.close();
    }

    @Override
    public void delete(Database db) throws SQLException {
        Statement stmt = getDataSource().getConnection().createStatement();
        stmt.executeUpdate(String.format(db.getExecutor().getDropDbStmt(), db.getName()));
        stmt.close();
    }

    @Override
    public Map<DatabasePK, Database> readAllFrom(Object ambient) throws SQLException {
        return readAll();
    }

    @Override
    public int count(Object ambient) throws SQLException {
        return readAllFrom(ambient).size();
    }
}
