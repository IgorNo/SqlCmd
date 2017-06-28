package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.datasource.BaseDataSource;
import ua.com.nov.model.entity.MetaData;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.AbstractMetaData;
import ua.com.nov.model.entity.metadata.server.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database extends BaseDataSource implements Unique<Database.Id>, MetaData {
    private final Id id;
    protected MetaDataOptions<Database> options;

    public Database(Server server, String dbName, MetaDataOptions<Database> options) {
        this.id = new Id(server, dbName);
        this.options = options;
    }

    public Database(Server server, String dbName) {
        this(server, dbName, null);
    }

    @Override
    public Id getId() {
        return id;
    }

    public Server getServer() {
        return getId().getServer();
    }

    @Override
    public Optional<? extends Database> getOptions() {
        return options;
    }

    @Override
    public Connection getConnection(String userName, String password) throws SQLException {
        Connection conn = DriverManager.getConnection(id.getServer().getName() + getName(), userName, password);
        if (id.server.getTableTypes() == null) id.server.init(conn);
        return conn;
    }

    @Override
    public String getType() {
        return id.getMdName();
    }

    @Override
    public String getViewName() {
        return id.getName();
    }

    public String getName() {
        return id.getName();
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

    @Override
    public String getCreateStmtDefinition(String conflictOption) {
        final StringBuilder sb = new StringBuilder(id.getMdName()).append(' ');
        if (conflictOption != null) sb.append(conflictOption).append(' ');
        sb.append(id.getName());
        if (options != null)
            sb.append(' ').append(options);
        return sb.toString();

    }

    @Override
    public String toString() {
        return getCreateStmtDefinition(null);
    }

    public static class Id extends AbstractMetaData.Id<Server.Id> {
        private final Server server;

        public Id(Server server, String dbName) {
            super(server.getId(), dbName);
            this.server = server;
        }

        @Override
        public String getMdName() {
            return "DATABASE";
        }

        @Override
        public Server getServer() {
            return server;
        }

        @Override
        public String getFullName() {
            return getName();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Id that = (Id) o;

            return getFullName().equalsIgnoreCase(that.getFullName());
        }

        @Override
        public int hashCode() {
            return getFullName().toLowerCase().hashCode();
        }
    }

}
