package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Test;
import ua.com.nov.model.dao.Dao;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.datatype.DataType;
import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.table.Column;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMdId;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static ua.com.nov.model.entity.metadata.datatype.DataType.NOT_NULL;

public abstract class AbstractTableDaoTest {

    protected static final Dao<TableId, Table, Database.DbId> TABLE_DAO = new TableDao();
    private  static final Dao<TableMdId, Column, TableId> COLUMN_DAO = new ColumnDao();

    private static DataSource dataSource;

    private static TableId customersId, productsId, ordersId;
    private static Table customers, products, orders;

    private DataType serial, integer, varchar, text, numeric, date;

    protected abstract Database getTestDatabase();

    public DataSource getDataSource() {
        return dataSource;
    }

    protected void createTestData(String catalog, String schema) throws SQLException {
        Database testDb = getTestDatabase();
        if (dataSource == null) {
            dataSource = new SingleConnectionDataSource(testDb);
            new DatabaseDao().setDataSource(dataSource).read(getTestDatabase().getId());
            TABLE_DAO.setDataSource(dataSource);
            COLUMN_DAO.setDataSource(dataSource);
        }
        serial = testDb.getAutoincrementalDataTypes(Types.INTEGER).get(0);
        integer = testDb.getMostApproximateDataTypes(JdbcDataTypes.INTEGER);
        varchar = testDb.getMostApproximateDataTypes(JdbcDataTypes.VARCHAR);
        text = testDb.getMostApproximateDataTypes(JdbcDataTypes.LONGVARCHAR);
        numeric = testDb.getMostApproximateDataTypes(JdbcDataTypes.NUMERIC);
        date = testDb.getMostApproximateDataTypes(JdbcDataTypes.DATE);

        customersId = new TableId(testDb.getId(), "Customers", catalog, schema);
        customers = new Table.Builder(customersId)
                .addColumn(new Column.Builder("id", serial).autoIncrement(true).nullable(NOT_NULL))
                .addColumn(new Column.Builder("name", varchar).size(100).nullable(NOT_NULL))
                .addColumn(new Column.Builder("phone", varchar).size(20).defaultValue("0"))
                .addColumn(new Column.Builder("address", varchar).size(150))
                .addColumn(new Column.Builder("rating", integer))
                .primaryKey(new PrimaryKey.Builder("id"))
                .addUniqueKey(new UniqueKey.Builder("name", "phone"))
                .build();

        productsId = new TableId(testDb.getId(), "Products", catalog, schema);
        products = new Table.Builder(productsId)
                .addColumn(new Column.Builder("id", serial).autoIncrement(true).nullable(NOT_NULL))
                .addColumn(new Column.Builder("description", varchar).size(100).nullable(NOT_NULL))
                .addColumn(new Column.Builder("details", text))
                .addColumn(new Column.Builder("price", numeric).size(8).precision(0).defaultValue("0"))
                .primaryKey(new PrimaryKey.Builder("id"))
                .build();

        ordersId = new TableId(testDb.getId(), "Orders", catalog, schema);
        orders = new Table.Builder(ordersId)
                .addColumn(new Column.Builder("id", serial).autoIncrement(true).nullable(NOT_NULL))
                .addColumn(new Column.Builder("date", date))
                .addColumn(new Column.Builder("product_id", integer).nullable(DataType.NOT_NULL))
                .addColumn(new Column.Builder("qty", integer))
                .addColumn(new Column.Builder("amount", numeric).size(10).precision(2))
                .addColumn(new Column.Builder("customer_id", integer))
                .primaryKey(new PrimaryKey.Builder("id"))
                .addForeignKey(new ForeignKey.Builder("product_id", products.getColumn("id"))
                        .deleteRule(ForeignKey.Rule.RESTRICT).updateRule(ForeignKey.Rule.CASCADE))
                .addForeignKey(new ForeignKey.Builder("customer_id", customers.getColumn("id"))
                        .deleteRule(ForeignKey.Rule.RESTRICT).updateRule(ForeignKey.Rule.CASCADE))
                .build();
    }

    public void setUp() throws SQLException {
        tearDown();
        TABLE_DAO.create(customers);
        TABLE_DAO.create(products);
        TABLE_DAO.create(orders);
    }

    @Test
    public void testReadTableMetaData() throws SQLException {
        Table table = TABLE_DAO.read(customers.getId());
        assertTrue(table.equals(customers));
        assertTrue(table.getColumns().size() == customers.getColumns().size());
        compareColumns(table, customers);
        table = TABLE_DAO.read(products.getId());
        assertTrue(table.equals(products));
        assertTrue(table.getColumns().size() == products.getColumns().size());
        compareColumns(table, products);
        table = TABLE_DAO.read(orders.getId());
        assertTrue(table.equals(orders));
        assertTrue(table.getColumns().size() == orders.getColumns().size());
        compareColumns(table, orders);
    }

    private void compareColumns(Table table1, Table table2) {
        for (Column col : table1.getColumns()) {
            Column testCol = table2.getColumn(col.getName());
            assertTrue(col.equals(testCol));
            if (!testCol.getName().equalsIgnoreCase("id") && !testCol.getName().equalsIgnoreCase("details")) {
                assertTrue(testCol.getDataType().equals(col.getDataType()));
                if (testCol.getDefaultValue() != null) {
                    assertTrue(col.getDefaultValue().contains(col.getDefaultValue()));
                }
            }
            if (testCol.getColumnSize() != null) {
                assertTrue(testCol.getColumnSize().equals(col.getColumnSize()));
            }
            if (testCol.getPrecision() != null) {
                assertTrue(testCol.getPrecision().equals(col.getPrecision()));
            }
            assertTrue(col.getNullable() == testCol.getNullable());
            assertTrue(col.isAutoIncrement() == testCol.isAutoIncrement());
            assertTrue(col.isGeneratedColumn() == testCol.isGeneratedColumn());
            if (testCol.getRemarks() != null)
                assertTrue(testCol.getRemarks().equals(col.getRemarks()));
        }
    }

    @Test
    public void testReadAllTables() throws SQLException{
        List<Table> tables = TABLE_DAO.readAll(getTestDatabase().getId());
        assertTrue(tables.contains(customers));
        assertTrue(tables.contains(products));
        assertTrue(tables.contains(orders));
    }

    @Test
    public void testRenameTable() throws SQLException{
        customers.setNewName("table11");
        TABLE_DAO.update(customers);
        TableId updateTableId = new TableId(customers.getDb().getId(), customers.getNewName(), customers.getCatalog(),
                customers.getSchema());
        assertTrue(TABLE_DAO.read(updateTableId).getNewName().equalsIgnoreCase(customers.getNewName()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void  testDeleteTable() throws SQLException {
        TABLE_DAO.delete(customers.getId());
        TABLE_DAO.read(customers.getId());
    }

    @Test
    public void testDeleteAllTables() throws SQLException {
        TABLE_DAO.deleteAll(getTestDatabase().getId());
        assertTrue(TABLE_DAO.readAll(getTestDatabase().getId()).size() == 0);
    }

    @Test
    public void testReadColumn() throws SQLException {
        Column testCol = customers.getColumn("name");
        Column readCol = COLUMN_DAO.read(testCol.getId());
        assertTrue(testCol.equals(readCol));
    }



    @After
    public void tearDown() throws SQLException {
        TABLE_DAO.deleteAll(getTestDatabase().getId());
    }

    protected static void tearDownClass() throws SQLException {
        dataSource.getConnection().close();
    }

    public Table getCustomers() {
        return customers;
    }

    public Table getProducts() {
        return products;
    }

    public Table getOrders() {
        return orders;
    }
}
