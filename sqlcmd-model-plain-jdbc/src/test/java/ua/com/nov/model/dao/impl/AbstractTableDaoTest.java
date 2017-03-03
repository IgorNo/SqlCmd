package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Test;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.table.Column;
import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.datatype.DataType;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static ua.com.nov.model.entity.metadata.datatype.DataType.*;

public abstract class AbstractTableDaoTest {

    public static final AbstractDao<TableId, Table, Database.DbId> DAO = new TableDao();

    private static DataSource dataSource;

    private static TableId customersId, productsId, tableId3;
    private static Table customers, products, table3;

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
        DataType serial = testDb.getAutoincrementalDataTypes(Types.INTEGER).get(0);
        DataType integer = testDb.getMostApproximateDataTypes(JdbcDataTypes.INTEGER);
        DataType varchar = testDb.getMostApproximateDataTypes(JdbcDataTypes.VARCHAR);
//        DataType text = testDb.getMostApproximateDataTypes(JdbcDataTypes.LONGVARCHAR);
        DataType numeric = testDb.getMostApproximateDataTypes(JdbcDataTypes.NUMERIC);

        customersId = new TableId(testDb.getId(), "Customers", catalog, schema);
        Column idColumnCustomers = new Column.Builder(customersId, "id", serial).autoIncrement(true).build();
        customers = new Table.Builder(customersId).addColumn(idColumnCustomers)
                .addColumn(new Column.Builder(customersId, "name", varchar).size(100).nullable(NO_NULL).build())
                .addColumn(new Column.Builder(customersId, "phone", varchar).size(20).build())
                .addColumn(new Column.Builder(customersId, "address", varchar).size(150).build())
                .addColumn(new Column.Builder(customersId, "rating", integer).build())
                .primaryKey(new PrimaryKey.Builder(customersId, "customer_id", idColumnCustomers).build())
                .build();

        productsId = new TableId(testDb.getId(), "Products", catalog, schema);
        Column idColumnProducts = new Column.Builder(productsId, "id", serial).autoIncrement(true).build();
        products = new Table.Builder(productsId).addColumn(idColumnProducts)
                .addColumn(new Column.Builder(productsId, "description", varchar).size(100).nullable(NO_NULL).build())
                .addColumn(new Column.Builder(productsId, "price", numeric).size(8).precision(2).defaultValue("0").build())
                .primaryKey(new PrimaryKey.Builder(productsId, "product_id", idColumnProducts).build())
                .build();

        tableId3 = new TableId(testDb.getId(), "Table3", catalog, schema);
        table3 = new Table.Builder(tableId3).
                addColumn(new Column.Builder(tableId3, "id3", serial).build()).
                build();
    }


    public void setUp() throws SQLException {
        tearDown();
        DAO.create(customers);
        DAO.create(products);
//        DAO.create(table3);
    }

    @Test
    public void testReaOne() throws SQLException {
        assertTrue(DAO.read(customers.getId()).equals(customers));
    }

    @Test
    public void testReadAllTables() throws SQLException{
        List<Table> tables = DAO.readAll(getTestDatabase().getId());
        assertTrue(tables.contains(customers));
        assertTrue(tables.contains(products));
//        assertTrue(tables.contains(table3));
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
        DAO.delete(customers.getId());
        DAO.read(customers.getId());
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
