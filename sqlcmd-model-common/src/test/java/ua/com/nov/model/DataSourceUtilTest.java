package ua.com.nov.model;

import org.junit.Test;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabaseID;
import ua.com.nov.model.entity.database.PostgreSqlDb;
import ua.com.nov.model.util.DataSourceUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class DataSourceUtilTest {

    public static final DataSource dataSource =
            new SingleConnectionDataSource(new PostgreSqlDb(
                    new DatabaseID(DataSourceUtil.POSTGRE_SQL_LOCAL_URL + "postgres", "postgres"),
                    "postgres"));

    @Test
    public void testGetDbName() throws SQLException{
        Database db = DataSourceUtil.getDatabase(dataSource.getConnection());
        assertTrue(db.getName().equals("postgres"));
    }
}
