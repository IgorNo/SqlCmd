package ua.com.nov.model.entity.database;

import ua.com.nov.model.datasource.BaseDataSource;
import ua.com.nov.model.entity.Mappable;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.table.TableId;
import ua.com.nov.model.repository.DbRepository;
import ua.com.nov.model.statement.AbstractSqlTableStatements;
import ua.com.nov.model.util.DbUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public abstract class Database extends BaseDataSource implements Persistent<Database> {
    private DatabaseId id;
    private String password;
    private List<DataType> dataTypes;
    private String dbProperties = "";

    public Database(DatabaseId id) {
        this(id, null);
    }

    public Database(String dbUrl, String userName) {
        this(new DatabaseId(dbUrl, userName), null);
    }

    public Database(String dbUrl, String userName, String password) {
        this(new DatabaseId(dbUrl, userName), password);
    }

    public Database(DatabaseId id, String password) {
        this.id = id;
        this.password = password;
        DbRepository.addDb(this);
    }

    public abstract AbstractSqlTableStatements getTableSqlStmtSource();

    public abstract String getFullTableName(TableId id);

    @Override
    public Mappable<Database> getRowMapper() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getDbUrl(), getUserName(), password);
    }

    // convert 'parameter' to database format (to upper or lower case)
    public abstract String convert(String parameter);

    public void setDataTypes(List<DataType> dataTypes) {
        this.dataTypes = dataTypes;
    }

    public DataType getDataType(String typeName) {
        for (DataType dataType : dataTypes) {
            if (dataType.getTypeName().equalsIgnoreCase(typeName)) return dataType;
        }
        throw new IllegalArgumentException();
    }

    public List<DataType> getListDataType(int jdbcDataType) {
        List<DataType> result = new LinkedList<>();
        for (DataType dataType : dataTypes) {
            if (jdbcDataType == dataType.getJdbcDataType()) result.add(dataType);
        }
        return result;
    }

    public String getDbProperties() {
        return dbProperties;
    }

    public void setDbProperties(String dbProperties) {
        this.dbProperties = dbProperties;
    }

    @Override
    public DatabaseId getId() {
        return id;
    }

    public String getDbUrl() {
        return id.getDbUrl();
    }

    public String getName() {
        return DbUtil.getDatabaseName(getDbUrl());
    }

    public String getUserName() {
        return id.getUserName();
    }

    public String getPassword() {
        return password;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Database database = (Database) o;

        return id.equals(database.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
