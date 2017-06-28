package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.entity.metadata.grantee.privelege.HyperSqlPrivilege;
import ua.com.nov.model.entity.metadata.grantee.user.HyperSqlUserOptions;

import java.sql.SQLException;

import static ua.com.nov.model.dao.impl.AbstractTableDaoTest.*;
import static ua.com.nov.model.entity.metadata.grantee.privelege.HyperSqlPrivilege.Action.*;

public class HyperSqlGranteeDaoTest extends AbstractGranteeDaoTest {

    @BeforeClass
    public static void setUpClass() throws DaoSystemException, SQLException {
        HyperSqlTableDaoTest.setUpClass();
        tableDaoTest = new HyperSqlTableDaoTest();
        AbstractGranteeDaoTest.setUpClass();
        createTestData(new HyperSqlUserOptions.Builder().build());
        updatedUserOptions = new HyperSqlUserOptions.Builder().password("0000").initialSchema("TMP_SCHEMA")
                .build();
        dbPrivilege = HyperSqlPrivilege.Builder.creteDatabasePrivileges(testDb, ALL)
                .addUser(user1).build();
        schemaPrivilege = HyperSqlPrivilege.Builder.creteSchemaPrivileges(testSchema, ALL)
                .addUser(user1).build();
        tablePrivilege1 = HyperSqlPrivilege.Builder.createTablePrivileges(customers,
                INSERT, SELECT, UPDATE, DELETE).addUser(user1).build();
        tablePrivilege2 = HyperSqlPrivilege.Builder.createTablePrivileges(testSchema, ALL)
                .addUser(user1).build();
        columnPrivilege = HyperSqlPrivilege.Builder.creteColumnPrivileges(orders)
                .addAction(SELECT, orders.getColumn("id")).addUser(user1).build();
    }

}
