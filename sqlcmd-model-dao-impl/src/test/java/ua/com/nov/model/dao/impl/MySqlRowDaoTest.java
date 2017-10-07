package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DAOSystemException;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.data.Row;

import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MySqlRowDaoTest extends AbstractRowDaoTest {

    @BeforeClass
    public static void setUpClass() throws DAOSystemException, SQLException {
        MySqlTableDaoTest.setUpClass();
        tableDaoTest = new MySqlTableDaoTest();
        AbstractRowDaoTest.setUpClass();
    }

    @Override
    @Test
    public void testUpdateRow() throws DAOSystemException {
        super.testUpdateRow();
        Row user = new Row.Builder(userList.get(0)).setValue("id", 1).setValue("login_", "update").build();
        ROW_DAO.update(user);
        AbstractRow result = ROW_DAO.read(user.getId());
        assertFalse(user.getValue("id").equals(result.getValue("id")));
        assertTrue(user.getValue("login").equals(result.getValue("login")));
        assertTrue(user.getValue("password").equals(result.getValue("password")));
        assertFalse(user.getValue("login_").equals(result.getValue("login_")));
    }
}