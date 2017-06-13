package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.entity.metadata.grantee.user.HyperSqlUserOptions;

import java.sql.SQLException;

public class HyperSqlGranteeDaoTest extends AbstractGranteeDaoTest {

    @BeforeClass
    public static void setUpClass() throws DaoSystemException, SQLException {
        HyperSqlSchemaDaoTest.setUpClass();
        HyperSqlTableDaoTest.setUpClass();
        schemaDaoTest = new HyperSqlSchemaDaoTest();
        tableDaoTest = new HyperSqlTableDaoTest();
        AbstractGranteeDaoTest.setUpClass();
        createTestData(new HyperSqlUserOptions.Builder("").build());
        updatedUserOptions = new HyperSqlUserOptions.Builder("0000").initialSchema("TMP_SCHEMA")
                .build();
    }

}
