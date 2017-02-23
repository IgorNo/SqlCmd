package ua.com.nov.model;

import org.junit.Test;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabaseId;
import ua.com.nov.model.entity.database.PostgreSqlDb;
import ua.com.nov.model.util.DbUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class DataSourceUtilTest {

    @Test
    public void testGetDbName() throws SQLException{
        DataSource dataSource =
                new SingleConnectionDataSource(new PostgreSqlDb(
                        new DatabaseId(DbUtil.POSTGRE_SQL_LOCAL_URL + "postgres", "postgres"),
                        "postgres"));
        Database db = DbUtil.getDatabase(dataSource.getConnection());
        assertTrue(db.getName().equals("postgres"));
    }
}
