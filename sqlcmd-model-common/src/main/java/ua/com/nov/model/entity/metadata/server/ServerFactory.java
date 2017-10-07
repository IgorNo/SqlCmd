package ua.com.nov.model.entity.metadata.server;

import ua.com.nov.model.dao.exception.BusinessLogicException;
import ua.com.nov.model.util.JdbcUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

public class ServerFactory {
    private ServerFactory() {
    }

    public static Server createServer(Connection conn) throws SQLException {
        String url = JdbcUtils.getDatabaseUrl(conn);
        StringBuilder serverName = new StringBuilder(JdbcUtils.getDatabaseProductName(url).toLowerCase());
        serverName.replace(0, 1, serverName.substring(0, 1).toUpperCase());
        String serverClassPakage = Server.class.getPackage().getName() + ".";
        serverName.insert(0, serverClassPakage).append("Server");
        try {
            Class[] paramTypes = new Class[]{String.class};
            Constructor<?> constructor = Class.forName(serverName.toString()).getConstructor(paramTypes);
            Server server = (Server) constructor.newInstance(new Object[]{url});
            server.init(conn);
            return server;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException e) {
            throw new BusinessLogicException("Server class doesn't exist.\n", e);
        }
    }
}
