package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.entity.metadata.grantee.user.MySqlUserOptions;

import java.sql.SQLException;

public class MySqlGranteeDaoTest extends AbstractGranteeDaoTest {

    @BeforeClass
    public static void setUpClass() throws DaoSystemException, SQLException {
        MySqlTableDaoTest.setUpClass();
        tableDaoTest = new MySqlTableDaoTest();
        AbstractGranteeDaoTest.setUpClass();
        createTestData(new MySqlUserOptions.Builder().password("")
                .addResourceOption(MySqlUserOptions.ResourceOption.MAX_CONNECTIONS_PER_HOUR, 10)
                .addResourceOption(MySqlUserOptions.ResourceOption.MAX_QUERIES_PER_HOUR, 11)
                .addResourceOption(MySqlUserOptions.ResourceOption.MAX_UPDATES_PER_HOUR, 12)
                .addResourceOption(MySqlUserOptions.ResourceOption.MAX_USER_CONNECTIONS, 13)
                .lockOption(MySqlUserOptions.LockOption.LOCK)
                .build());
        updatedUserOptions = new MySqlUserOptions.Builder().password("0000")
                .addResourceOption(MySqlUserOptions.ResourceOption.MAX_CONNECTIONS_PER_HOUR, 101)
                .addResourceOption(MySqlUserOptions.ResourceOption.MAX_QUERIES_PER_HOUR, 111)
                .addResourceOption(MySqlUserOptions.ResourceOption.MAX_UPDATES_PER_HOUR, 112)
                .addResourceOption(MySqlUserOptions.ResourceOption.MAX_USER_CONNECTIONS, 113)
                .lockOption(MySqlUserOptions.LockOption.UNLOCK)
                .build();
    }

}
