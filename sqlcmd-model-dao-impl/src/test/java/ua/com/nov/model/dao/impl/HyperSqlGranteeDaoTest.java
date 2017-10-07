package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DAOSystemException;
import ua.com.nov.model.entity.metadata.grantee.privelege.HyperSqlPrivilege;
import ua.com.nov.model.entity.metadata.grantee.privelege.OnDeleteOptions;
import ua.com.nov.model.entity.metadata.grantee.user.HyperSqlUserOptions;

import java.sql.SQLException;

import static ua.com.nov.model.dao.impl.AbstractTableDaoTest.customers;
import static ua.com.nov.model.entity.metadata.grantee.privelege.HyperSqlPrivilege.Action.*;

public class HyperSqlGranteeDaoTest extends AbstractGranteeDaoTest {

    @BeforeClass
    public static void setUpClass() throws DAOSystemException, SQLException {
        HyperSqlTableDaoTest.setUpClass();
        tableDaoTest = new HyperSqlTableDaoTest();
        AbstractGranteeDaoTest.setUpClass();
        createTestData(new HyperSqlUserOptions.Builder().build());
        updatedUserOptions = new HyperSqlUserOptions.Builder().password("0000").initialSchema("TMP_SCHEMA")
                .build();
        tablePrivilege1 = HyperSqlPrivilege.Builder.createTablePrivileges(customers,
                INSERT, SELECT, UPDATE, DELETE).addUser(user1).build();
    }

    @Override
    @Test
    public void testGrantRevokePrivilege() throws DAOSystemException {
        PRIVILEGE_DAO.grant(tablePrivilege1);
        PRIVILEGE_DAO.revoke(tablePrivilege1, OnDeleteOptions.RESTRICT);
    }
}
