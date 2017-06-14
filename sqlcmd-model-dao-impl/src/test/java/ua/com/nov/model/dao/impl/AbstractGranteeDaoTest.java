package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DaoBusinessLogicException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.grantee.user.User;
import ua.com.nov.model.entity.metadata.grantee.user.UserOptions;
import ua.com.nov.model.entity.metadata.server.Server;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public abstract class AbstractGranteeDaoTest {
    protected static final UserDao USER_DAO = new UserDao();

    protected static AbstractSchemaDaoTest schemaDaoTest;
    protected static AbstractTableDaoTest tableDaoTest;

    protected static Server server;

    protected static User user1;
    protected static UserOptions updatedUserOptions;

    protected static void setUpClass() throws DaoSystemException, SQLException {
        schemaDaoTest.setUp();
        tableDaoTest.setUp();
        server = AbstractSchemaDaoTest.schemaId.getServer();
        DataSource dataSource = new SingleConnectionDataSource(AbstractSchemaDaoTest.testDb, "root", "root");
        USER_DAO.setDataSource(dataSource);
    }

    protected static void createTestData(UserOptions options) {
        user1 = server.getUserBuilder(server.getId(), "user1", options).build();
    }

    @AfterClass
    public static void tearDownClass() throws SQLException, DaoSystemException {
        USER_DAO.getDataSource().getConnection().close();
        schemaDaoTest.tearDown();
    }

    @Before
    public void setUp() throws DaoSystemException {
        tearDown();
        USER_DAO.create(user1);
    }

    @Test
    public void readUser() throws DaoSystemException {
        User result = USER_DAO.read(user1.getId());
        assertTrue(user1.equals(result));
        AbstractTableDaoTest.compareOptions(user1.getOptions(), result.getOptions());
    }

    @Test
    public void readAllUsers() throws DaoSystemException {
        List<User> users = USER_DAO.readAll(user1.getId().getServer().getId());
        assertTrue(users.contains(user1));
    }

    @Test(expected = DaoBusinessLogicException.class)
    public void deleteUser() throws DaoSystemException {
        USER_DAO.delete(user1);
        USER_DAO.read(user1.getId());
        assertTrue(false);
    }

    @Test
    public void updateUser() throws DaoSystemException {
        User updatedUser = server.getUserBuilder(user1.getServer().getId(), user1.getName(), updatedUserOptions).build();
        USER_DAO.update(updatedUser);
        User result = USER_DAO.read(updatedUser.getId());
        assertTrue(updatedUser.equals(result));
        assertTrue(!result.getPassword().isEmpty());
        AbstractTableDaoTest.compareOptions(updatedUserOptions, result.getOptions());
    }

    @After
    public void tearDown() throws DaoSystemException {
        List<User> users = USER_DAO.readAll(AbstractSchemaDaoTest.schema.getServer().getId());
        if (users.contains(user1)) USER_DAO.delete(user1);
    }

}