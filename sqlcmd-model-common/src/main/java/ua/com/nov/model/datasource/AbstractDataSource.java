package ua.com.nov.model.datasource;

import ua.com.nov.model.entity.database.DataType;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.repository.DbRepository;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractDataSource implements DataSource {
    private Database db;

    public AbstractDataSource(Database db) {
            this.db = DbRepository.getInstance().addDb(db);
    }

    protected Database getDb() {
        return db;
    }

    protected static Connection getConnection(Database db, String userName, String password) throws  SQLException {
        Connection conn = DriverManager.getConnection(db.getDbUrl(), userName, password);
        if (db.getDataTypes() == null) {
            List<DataType> dataTypes = new LinkedList<>();
            ResultSet rs = conn.getMetaData().getTypeInfo();
            while (rs.next()) {
                DataType dataType = new DataType.Builder(rs.getString("TYPE_NAME"), rs.getInt("DATA_TYPE"))
                        .precision(rs.getInt("PRECISION"))
                        .literalPrefix(rs.getString("LITERAL_PREFIX"))
                        .literalSuffix(rs.getString("LITERAL_SUFFIX"))
                        .createParams(rs.getString("CREATE_PARAMS"))
                        .nullable(rs.getShort("NULLABLE"))
                        .caseSensitive(rs.getBoolean("CASE_SENSITIVE"))
                        .searchable(rs.getShort("SEARCHABLE"))
                        .unsignedAttribute(rs.getBoolean("UNSIGNED_ATTRIBUTE"))
                        .fixedPrecScale(rs.getBoolean("FIXED_PREC_SCALE"))
                        .autoIncrement(rs.getBoolean("AUTO_INCREMENT"))
                        .localTypeName(rs.getString("LOCAL_TYPE_NAME"))
                        .minimumScale(rs.getInt("MINIMUM_SCALE"))
                        .maximumScale(rs.getInt("MAXIMUM_SCALE"))
                        .numPrecRadix(rs.getInt("NUM_PREC_RADIX"))
                        .build();
                dataTypes.add(dataType);
            }
            db.setDataTypes(dataTypes);
        }
        return conn;
    }

    @Override
    public Connection getConnection() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Connection getConnection(String userName, String password) throws SQLException {
        throw new UnsupportedOperationException();
    }


    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException();
    }
}
