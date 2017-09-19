package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.dao.exception.MappingBusinessLogicException;
import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.entity.metadata.grantee.privelege.Privilege;
import ua.com.nov.model.entity.metadata.grantee.user.User;
import ua.com.nov.model.entity.metadata.grantee.user.UserOptions;
import ua.com.nov.model.entity.metadata.server.Server;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public abstract class AbstractGranteeDaoTest {
    protected static final UserDao USER_DAO = new UserDao();
    protected static final PrivilegeDao PRIVILEGE_DAO = new PrivilegeDao();

    protected static AbstractTableDaoTest tableDaoTest;

    protected static Server server;

    protected static User user1;
    protected static UserOptions updatedUserOptions;

    protected static Privilege columnPrivilege, tablePrivilege1, tablePrivilege2, schemaPrivilege, dbPrivilege;

    protected static void setUpClass() throws MappingSystemException, SQLException {
        tableDaoTest.setUp();
        server = tableDaoTest.testDb.getServer();
        USER_DAO.setDataSource(tableDaoTest.dataSource);
        PRIVILEGE_DAO.setDataSource(tableDaoTest.dataSource);
    }

    protected static void createTestData(UserOptions options) {
        user1 = server.getUserBuilder(server.getId(), "user1", options).build();
    }

    @AfterClass
    public static void tearDownClass() throws SQLException, MappingSystemException {
        tableDaoTest.tearDown();
        USER_DAO.getDataSource().getConnection().close();
    }

    @Before
    public void setUp() throws MappingSystemException {
        tearDown();
        USER_DAO.create(user1);
    }

    @Test
    public void testGrantRevokePrivilege() throws MappingSystemException {
        PRIVILEGE_DAO.grant(tablePrivilege1);
        PRIVILEGE_DAO.grant(columnPrivilege);
        PRIVILEGE_DAO.grant(tablePrivilege2);
        PRIVILEGE_DAO.grant(schemaPrivilege);
        PRIVILEGE_DAO.grant(dbPrivilege);

        PRIVILEGE_DAO.revoke(tablePrivilege1, null);
        PRIVILEGE_DAO.revoke(columnPrivilege, null);
        PRIVILEGE_DAO.revoke(tablePrivilege2, null);
        PRIVILEGE_DAO.revoke(schemaPrivilege, null);
        PRIVILEGE_DAO.revoke(dbPrivilege, null);
    }

    @Test
    public void testReadUser() throws MappingSystemException {
        User result = USER_DAO.read(user1.getId());
        assertTrue(user1.equals(result));
        AbstractTableDaoTest.compareOptions(user1.getOptions(), result.getOptions());
    }

    @Test
    public void testReadAllUsers() throws MappingSystemException {
        List<User> users = USER_DAO.readAll(user1.getId().getServer().getId());
        assertTrue(users.contains(user1));
    }

    @Test(expected = MappingBusinessLogicException.class)
    public void testDeleteUser() throws MappingSystemException {
        USER_DAO.delete(user1);
        USER_DAO.read(user1.getId());
        assertTrue(false);
    }

    @Test
    public void testUpdateUser() throws MappingSystemException {
        User updatedUser = server.getUserBuilder(user1.getServer().getId(), user1.getName(), updatedUserOptions).build();
        USER_DAO.update(updatedUser);
        User result = USER_DAO.read(updatedUser.getId());
        assertTrue(updatedUser.equals(result));
        assertTrue(!result.getPassword().isEmpty());
        AbstractTableDaoTest.compareOptions(updatedUserOptions, result.getOptions());
    }

    @After
    public void tearDown() throws MappingSystemException {
        List<User> users = USER_DAO.readAll(server.getId());
        if (users.contains(user1)) USER_DAO.delete(user1);
    }

}
