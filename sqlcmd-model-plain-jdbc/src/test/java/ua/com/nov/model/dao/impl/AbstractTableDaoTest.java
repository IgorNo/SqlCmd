package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Test;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.table.metadata.column.Column;
import ua.com.nov.model.entity.metadata.table.metadata.column.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.database.DataType;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableId;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import static org.junit.Assert.assertTrue;

public abstract class AbstractTableDaoTest {

    public static final AbstractDao<TableId, Table, Database.DbId> DAO = new TableDao();

    private static DataSource dataSource;

    private static TableId customersId, tableId2, tableId3;
    private static Table customers, table2, table3;

    protected abstract Database getTestDatabase();

    protected static DataSource getDataSource() {
        return dataSource;
    }

    protected void createTestData(String catalog, String schema) throws SQLException {
        Database testDb = getTestDatabase();
        if (dataSource == null) {
            dataSource = new SingleConnectionDataSource(testDb);
            new DatabaseDao().setDataSource(dataSource).read(getTestDatabase().getId());
            DAO.setDataSource(dataSource);
        }
        DataType intAutoincrementDataType = testDb.getAutoincrementalDataTypes(Types.INTEGER).get(0);
        DataType intDataType = testDb.getMostApproximateDataTypes(JdbcDataTypes.INTEGER);
        DataType varcharDataType = testDb.getMostApproximateDataTypes(JdbcDataTypes.VARCHAR);

        customersId = new TableId(testDb.getId(), "Customers", catalog, schema);
        customers = new Table.Builder(customersId)
                .addColumn(new Column.Builder(customersId, "id", intAutoincrementDataType)
                .autoIncrement(true).build())
                .addColumn(new Column.Builder(customersId, "name", varcharDataType)
                .columnSize(100).build())
                .addColumn(new Column.Builder(customersId, "phone", varcharDataType)
                .columnSize(20).build())
                .addColumn(new Column.Builder(customersId, "address", varcharDataType)
                .columnSize(150).build())
                .addColumn(new Column.Builder(customersId, "rating", intDataType).build())
                .build();

        tableId2 = new TableId(testDb.getId(), "Table2", catalog, schema);
        table2 = new Table.Builder(tableId2).
                addColumn(new Column.Builder(tableId2, "id2", intAutoincrementDataType).build()).
                build();

        tableId3 = new TableId(testDb.getId(), "Table3", catalog, schema);
        table3 = new Table.Builder(tableId3).
                addColumn(new Column.Builder(tableId3, "id3", intAutoincrementDataType).build()).
                build();
    }


    public void setUp() throws SQLException {
        tearDown();
        DAO.create(customers);
        DAO.create(table2);
        DAO.create(table3);
    }

    @Test
    public void testReaOne() throws SQLException {
        assertTrue(DAO.read(table3.getId()).equals(table3));
    }

    @Test
    public void testReadAllTables() throws SQLException{
        List<Table> tables = DAO.readAll(getTestDatabase().getId());
        assertTrue(tables.contains(customers));
        assertTrue(tables.contains(table2));
        assertTrue(tables.contains(table3));
    }

    @Test
    public void testRenameTable() throws SQLException{
        customers.setNewName("table11");
        DAO.update(customers);
        TableId updateTableId = new TableId(customers.getDb().getId(), customers.getNewName(), customers.getCatalog(),
                customers.getSchema());
        assertTrue(DAO.read(updateTableId).getNewName().equalsIgnoreCase(customers.getNewName()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void  testDeleteTable() throws SQLException {
        DAO.delete(table2.getId());
        DAO.read(table2.getId());
    }

    @Test
    public void testDeleteAllTables() throws SQLException {
        DAO.deleteAll(getTestDatabase().getId());
        assertTrue(DAO.readAll(getTestDatabase().getId()).size() == 0);
    }

    @After
    public void tearDown() throws SQLException {
        DAO.deleteAll(getTestDatabase().getId());
    }

    protected static void tearDownClass() throws SQLException {
        dataSource.getConnection().close();
    }

}
