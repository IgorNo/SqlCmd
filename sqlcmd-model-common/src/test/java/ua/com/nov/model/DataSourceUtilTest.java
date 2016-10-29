package ua.com.nov.model;

import org.junit.Test;
import ua.com.nov.model.entity.Database;
import ua.com.nov.model.util.DataSourceUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class DataSourceUtilTest {

    public static final DataSource dataSource =
            new SingleConnectionDataSource(new Database(DataSourceUtil.POSTGRE_SQL_LOCAL_URL + "postgres",
                    "postgres", "postgres"));

    @Test
    public void testGetDbName() throws SQLException{
        assertTrue(DataSourceUtil.getDatabase(dataSource.getConnection()).getDbUrl().equals("postgres"));
    }
}
