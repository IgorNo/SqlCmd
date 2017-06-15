package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.entity.metadata.grantee.user.PostgreSqlUserOptions;

import java.sql.Date;
import java.sql.SQLException;

public class PostgreSqlGranteeDaoTest extends AbstractGranteeDaoTest {

    @BeforeClass
    public static void setUpClass() throws DaoSystemException, SQLException {
        PostgreSqlTableDaoTest.setUpClass();
        tableDaoTest = new HyperSqlTableDaoTest();
        AbstractGranteeDaoTest.setUpClass();
        createTestData(new PostgreSqlUserOptions.Builder()
                .password("NULL").connectionLimit(10).validUntil(new Date(118, 06, 15)).superUser(false)
                .canLogin(false).bypassRLS(false).createDb(false).createRole(false).inherit(false).replication(false)
                .build());
        updatedUserOptions = new PostgreSqlUserOptions.Builder().password("0000")
                .connectionLimit(10).validUntil(new Date(118, 05, 15)).superUser(true)
                .canLogin(true).bypassRLS(true).createDb(true).createRole(true).inherit(true).replication(true)
                .build();
    }

}
