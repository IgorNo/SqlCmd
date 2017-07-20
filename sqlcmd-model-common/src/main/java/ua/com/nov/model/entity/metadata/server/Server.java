package ua.com.nov.model.entity.metadata.server;

import ua.com.nov.model.dao.statement.*;
import ua.com.nov.model.entity.MetaDataId;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.AbstractMetaData;
import ua.com.nov.model.entity.metadata.NullMetaData;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.datatype.DataTypes;
import ua.com.nov.model.entity.metadata.datatype.DbDataType;
import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.grantee.user.User;
import ua.com.nov.model.entity.metadata.grantee.user.UserOptions;
import ua.com.nov.model.entity.metadata.schema.Schema;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.column.ColumnOptions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class Server  implements Unique<Server.Id> {
    protected List<String> tableTypes;
    private Id id;
    private Map<String, DbDataType> dataTypes = new HashMap<>();
    private Map<DataTypes, String> typesMap = new HashMap<>();
    private Map<String, Grantee> granteeMap = new HashMap<>();

    public Server(String url) {
        this.id = new Id(url);
    }

    public Grantee getUser(String roleName) {
        return granteeMap.get(roleName);
    }

    public Grantee addUser(Grantee role) {
        return granteeMap.put(role.getName(), role);
    }

    @Override
    public Id getId() {
        return id;
    }

    public String getName() {
        return id.getName();
    }

    public void init(Connection conn) throws SQLException{
        addDataTypes(getDataTypes(conn));
        tableTypes = getTableTypes(conn);
    }

    private List<DbDataType> getDataTypes(Connection conn) throws SQLException {
        ResultSet rs = conn.getMetaData().getTypeInfo();
        List<DbDataType> dataTypeList = new ArrayList<>();
        while (rs.next()) {
            DbDataType dataType = new DbDataType.Builder(rs.getString("TYPE_NAME"), rs.getInt("DATA_TYPE"))
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
            dataTypeList.add(dataType);
        }
        return dataTypeList;
    }

    private List<String> getTableTypes(Connection conn) throws SQLException {
        ResultSet rs = conn.getMetaData().getTableTypes();
        List<String> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rs.getString("TABLE_TYPE"));
        }
        return result;
    }

    public abstract ColumnOptions.Builder<? extends ColumnOptions> createColumnOptions();

    public OptionsSqlStmtSource getOptionsSqlStmtSource(String mdName) {
        switch (mdName) {
            case "DATABASE":
                return getDatabaseOptionsSqlStmtSource();

            case "TABLE":
                return getTableOptionsSqlStmtSource();

            case "COLUMN":
                return getColumnOptionsSqlStmSource();

            case "USER":
                return getUserOptionsSqlStmSource();

            default:
                throw new IllegalArgumentException();
        }
    }

    protected OptionsSqlStmtSource<Database.Id, ? extends Database> getDatabaseOptionsSqlStmtSource() {
        return new OptionsSqlStmtSource<Database.Id, Database>() {
        };
    }

    protected OptionsSqlStmtSource<Table.Id, Table> getTableOptionsSqlStmtSource() {
        return new OptionsSqlStmtSource<Table.Id, Table>() {
        };
    }

    protected OptionsSqlStmtSource<Column.Id, Column> getColumnOptionsSqlStmSource() {
        return new OptionsSqlStmtSource<Column.Id, Column>() {
        };
    }

    public abstract OptionsSqlStmtSource<Grantee.Id, User> getUserOptionsSqlStmSource();

    public abstract AbstractDatabaseMdSqlStatements<Database.Id, Database, Id> getDatabaseSqlStmtSource();

    public AbstractDatabaseMdSqlStatements<Grantee.Id, User, Id> getUserSqlStmtSource() {
        return new AbstractDatabaseMdSqlStatements<Grantee.Id, User, Id>() {
            @Override
            public SqlStatement getReadOneStmt(Grantee.Id eId) {
                return eId.getServer().getUserOptionsSqlStmSource().getReadOptionsStmt(eId);
            }

            @Override
            public SqlStatement getReadAllStmt(Id cId) {
                return cId.getServer().getUserOptionsSqlStmSource().getReadAllOptionsStmt();
            }
        };
    }

    public <I extends AbstractMetaData.Id<C>, E extends AbstractMetaData<I>, C extends MetaDataId>
    AbstractDatabaseMdSqlStatements<I, E, C> getDatabaseMdSqlStmtSource() {
        return new AbstractDatabaseMdSqlStatements() {
        };
    }

    public AbstractDatabaseMdSqlStatements<Table.Id, Table, Schema.Id> getTableSqlStmtSource() {
        return new AbstractDatabaseMdSqlStatements<Table.Id, Table, Schema.Id>() {
            @Override
            protected String getCommentStmt(Table table) {
                AbstractTableMdSqlStatements sqlStmtSource = table.getServer().getTableMdSqlStmtSource();
                StringBuilder sb = new StringBuilder(super.getCommentStmt(table));
                String s = "";
                for (Column column : table.getColumns()) {
                    if (column.getViewName() != null) {
                        sb.append(s).append(sqlStmtSource.getCommentStmt(column));
                        //                      s = ";";
                    }
                }
                return sb.toString();
            }
        };
    }

    public <I extends TableMd.Id, E extends TableMd>
    AbstractTableMdSqlStatements<I, E> getTableMdSqlStmtSource() {
        return new AbstractTableMdSqlStatements<I, E>() {
        };
    }

    public AbstractTableMdSqlStatements<Column.Id, Column> getColumnSqlStmtSource() {
        return new AbstractTableMdSqlStatements<Column.Id, Column>() {
        };
    }

    public AbstractPrivilegeStatements getPrivelegeStmtSource() {
        return new AbstractPrivilegeStatements() {
        };
    }

    // convert 'parameter' to database format (to upper or lower case)
    public abstract String convert(String parameter);

    public void addDataTypes(Collection<DbDataType> dataTypeList) {
        for (DbDataType dataType : dataTypeList) {
            dataTypes.put(dataType.getTypeName(), dataType);
        }
    }


    public DbDataType getDataType(String typeName) {
        DbDataType dataType = dataTypes.get(typeName);
        if (dataType == null)
            throw new IllegalArgumentException(String.format("Data type '%s' dosn't exist in this database", typeName));
        return dataType;
    }

    public void addTableTypes(Collection<String> tableTypes) {
        this.tableTypes = new ArrayList<>(tableTypes);
    }

    public String[] getTableTypes() {
        if (tableTypes != null)
            return tableTypes.toArray(new String[0]);
        else
            return null;
    }

    /**
     * @param jdbcDataType
     * @return List of DbDataType elements of that have jdbcDataType
     */
    public List<DbDataType> getDataTypes(int jdbcDataType) {
        List<DbDataType> result = new LinkedList<>();
        for (DbDataType dataType : dataTypes.values()) {
            if (jdbcDataType == dataType.getJdbcDataType()) result.add(dataType);
        }
        return result;
    }

    /**
     * @param jdbcDataType
     * @return List of DbDataType elements of that have jdbcDataType and are autoincremental
     */
    public List<DbDataType> getAutoincrementalDataTypes(int jdbcDataType) {
        List<DbDataType> result = new LinkedList<>();
        for (DbDataType dataType : dataTypes.values()) {
            if (dataType.isAutoIncrement() && jdbcDataType == dataType.getJdbcDataType()) result.add(dataType);
        }
        return result;
    }

    public DbDataType getMostApproximateDataTypes(DataTypes type) {
        String dataTypeName = getTypesMap().get(type);
        if (dataTypeName != null)
            return getDataType(dataTypeName);
        else {
            List<DbDataType> dataTypes = getDataTypes(type.getJdbcDataType());
            if (dataTypes.size() == 0) {
                throw new IllegalArgumentException(String.format("Data type %s does not support this database", type));
            }
            for (DbDataType dataType : dataTypes) {
                if (dataType.getTypeName().equalsIgnoreCase(type.toString())) return dataType;
            }
            return dataTypes.get(0);
        }
    }

    protected Map<DataTypes, String> getTypesMap() {
        return typesMap;
    }

    public abstract User.Builder getUserBuilder(Id id, String name, UserOptions options);

    public AbstractDataStmtSource getDataSqlStmtSource() {
        return new AbstractDataStmtSource() {
        };
    }

    public class Id extends AbstractMetaData.Id<NullMetaData> {

        public Id(String url) {
            super(new NullMetaData(), url);
        }

        @Override
        public Server getServer() {
            return Server.this;
        }

        @Override
        public String getMdName() {
            return null;
        }
    }
}
