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
import ua.com.nov.model.entity.metadata.table.constraint.Constraint;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static ua.com.nov.model.entity.metadata.datatype.DataType.NOT_NULL;

public abstract class AbstractTableDaoTest {

    protected static final Dao<TableId, Table, Database.DbId> TABLE_DAO = new TableDao();
    protected   static final Dao<TableMdId, Column, TableId> COLUMN_DAO = new ColumnDao();
    protected   static final Dao<TableMdId, PrimaryKey, TableId> PRIMARY_KEY_DAO = new PrimaryKeyDao();
    protected   static final Dao<TableMdId, ForeignKey, TableId> FOREIGN_KEY_DAO = new ForeignKeyDao();

    private static DataSource dataSource;

    protected static Table customers, products, orders, users;

    private DataType serial, integer, varchar, text, numeric, date;

    protected abstract Database getTestDatabase();

    public DataSource getDataSource() {
        return dataSource;
    }

    protected void createTestData(String catalog, String schema, String aiTypeName) throws SQLException {
        Database testDb = getTestDatabase();
        if (dataSource == null) {
            dataSource = new SingleConnectionDataSource(testDb);
            new DatabaseDao().setDataSource(dataSource).read(getTestDatabase().getId());
            TABLE_DAO.setDataSource(dataSource);
            COLUMN_DAO.setDataSource(dataSource);
            PRIMARY_KEY_DAO.setDataSource(dataSource);
            FOREIGN_KEY_DAO.setDataSource(dataSource);
        }
        serial = testDb.getDataType(aiTypeName);
        integer = testDb.getMostApproximateDataTypes(JdbcDataTypes.INTEGER);
        varchar = testDb.getMostApproximateDataTypes(JdbcDataTypes.VARCHAR);
        text = testDb.getMostApproximateDataTypes(JdbcDataTypes.LONGVARCHAR);
        numeric = testDb.getMostApproximateDataTypes(JdbcDataTypes.NUMERIC);
        date = testDb.getMostApproximateDataTypes(JdbcDataTypes.DATE);

        TableId customersId = new TableId(testDb.getId(), "Customers", catalog, schema);
        customers = new Table.Builder(customersId)
                .addColumn(new Column.Builder("id", serial).autoIncrement(true).nullable(NOT_NULL))
                .addColumn(new Column.Builder("name", varchar).size(100).nullable(NOT_NULL))
                .addColumn(new Column.Builder("phone", varchar).size(20).defaultValue("0"))
                .addColumn(new Column.Builder("address", varchar).size(150))
                .addColumn(new Column.Builder("rating", integer))
                .primaryKey(new PrimaryKey.Builder("id"))
                .addUniqueKey(new UniqueKey.Builder("name", "phone"))
                .build();

        TableId productsId = new TableId(testDb.getId(), "Products", catalog, schema);
        products = new Table.Builder(productsId)
                .addColumn(new Column.Builder("id", serial).autoIncrement(true).nullable(NOT_NULL))
                .addColumn(new Column.Builder("description", varchar).size(100).nullable(NOT_NULL))
                .addColumn(new Column.Builder("details", text))
                .addColumn(new Column.Builder("price", numeric).size(8).precision(0).defaultValue("0"))
                .primaryKey(new PrimaryKey.Builder("id"))
                .build();

        TableId ordersId = new TableId(testDb.getId(), "Orders", catalog, schema);
        orders = new Table.Builder(ordersId)
                .addColumn(new Column.Builder("id", serial).autoIncrement(true).nullable(NOT_NULL))
                .addColumn(new Column.Builder("date", date))
                .addColumn(new Column.Builder("product_id", integer).nullable(DataType.NOT_NULL))
                .addColumn(new Column.Builder("qty", integer))
                .addColumn(new Column.Builder("amount", numeric).size(10).precision(2))
                .addColumn(new Column.Builder("customer_id", integer))
                .primaryKey(new PrimaryKey.Builder("id"))
                .addForeignKey(new ForeignKey.Builder("product_id", products.getColumn("id").getId())
                        .deleteRule(ForeignKey.Rule.NO_ACTION).updateRule(ForeignKey.Rule.NO_ACTION))
                .addForeignKey(new ForeignKey.Builder("customer_id", customers.getColumn("id").getId())
                        .deleteRule(ForeignKey.Rule.NO_ACTION).updateRule(ForeignKey.Rule.NO_ACTION))
                .build();

        TableId usersId = new TableId(testDb.getId(), "Users", catalog, schema);
        users = new Table.Builder(usersId)
                .addColumn(new Column.Builder("login", varchar).size(25))
                .addColumn(new Column.Builder("password",varchar).size(25))
                .primaryKey(new PrimaryKey.Builder("login"))
                .build();
    }

    public void setUp() throws SQLException {
        tearDown();
        TABLE_DAO.create(customers);
        TABLE_DAO.create(products);
        TABLE_DAO.create(orders);
        TABLE_DAO.create(users);
    }

    @Test
    public void testReadTableMetaData() throws SQLException {
        Table table = TABLE_DAO.read(customers.getId());
        assertTrue(table.equals(customers));
        assertTrue(table.getPrimaryKey().equals(customers.getPrimaryKey()));
        assertTrue(table.getColumns().size() == customers.getColumns().size());
        compareColumns(table, customers);
        table = TABLE_DAO.read(products.getId());
        assertTrue(table.equals(products));
        assertTrue(table.getColumns().size() == products.getColumns().size());
        compareColumns(table, products);
        table = TABLE_DAO.read(orders.getId());
        assertTrue(table.equals(orders));
        compareColumns(table, orders);
        assertTrue(table.getColumns().size() == orders.getColumns().size());
        assertTrue(orders.getForeignKeyList().size() == table.getForeignKeyList().size());
        for (ForeignKey key : orders.getForeignKeyList()) {
            assertTrue(table.getForeignKeyList().contains(key));
        }
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
        TABLE_DAO.delete(orders.getId());
        TABLE_DAO.read(orders.getId());
        assertTrue(false);
    }

    @Test
    public void testDeleteAllTables() throws SQLException {
        TABLE_DAO.delete(orders.getId());
        TABLE_DAO.deleteAll(getTestDatabase().getId());
        assertTrue(TABLE_DAO.readAll(getTestDatabase().getId()).size() == 0);
    }

    @Test
    public void testAddColumn() throws SQLException {
        Column testCol = new Column.Builder(customers.getId(), "test", integer).build();
        COLUMN_DAO.create(testCol);
        Column readCol = COLUMN_DAO.read(testCol.getId());
        assertTrue(testCol.equals(readCol));
    }

    @Test
    public void testReadColumn() throws SQLException {
        Column testCol = customers.getColumn("name");
        Column readCol = COLUMN_DAO.read(testCol.getId());
        assertTrue(testCol.equals(readCol));
    }

    @Test
    public void testRenameColumn() throws SQLException {
        Column testCol = customers.getColumn("name");
        testCol.setNewName("test");
        COLUMN_DAO.update(testCol);
        Column readCol = COLUMN_DAO.read(new TableMdId(customers.getId(), "test"));
        assertTrue(readCol.getNewName().equalsIgnoreCase(testCol.getNewName()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void  testDeleteColumn() throws SQLException {
        COLUMN_DAO.delete(customers.getColumn("address").getId());
        COLUMN_DAO.read(customers.getColumn("address").getId());
        assertTrue(false);
    }

    @Test
    public void testDeleteAddReadPrimaryKey() throws SQLException {
        PRIMARY_KEY_DAO.delete(users.getPrimaryKey().getId());
        PrimaryKey pkTest = new PrimaryKey.Builder(users.getId(), "password").build();
        PRIMARY_KEY_DAO.create(pkTest);
        PrimaryKey pkRead = PRIMARY_KEY_DAO.read(pkTest.getId());
        assertTrue(pkRead.equals(pkTest));
    }

    @Test
    public void testRenamePrimaryKey() throws SQLException {
        PrimaryKey testPk = customers.getPrimaryKey();
        testPk.setNewName("test");
        PRIMARY_KEY_DAO.update(testPk);
        PrimaryKey readPk = PRIMARY_KEY_DAO.read(testPk.getId());
        assertTrue(readPk.equals(testPk));
    }

    @Test
    public void testDeleteAddReadForeignKey() throws SQLException {
        ForeignKey fk = orders.getForeignKeyList().get(0);
        FOREIGN_KEY_DAO.delete(fk.getId());
        try {
            FOREIGN_KEY_DAO.read(fk.getId());
            assertTrue(false);
        } catch (IllegalArgumentException e) {/*NOP*/}
        FOREIGN_KEY_DAO.create(fk);
        Table readTable = TABLE_DAO.read(orders.getId());
        assertTrue(fk.equals(readTable.getForeignKey(fk.getName())));
    }

    @Test
    public void testRenameForeignKey() throws SQLException {
        ForeignKey testFk = orders.getForeignKeyList().get(0);
        testFk.setNewName("test");
        FOREIGN_KEY_DAO.update(testFk);
        ForeignKey readFk = FOREIGN_KEY_DAO.read(new Constraint.ConstraintId(orders.getId(), "test"));
        assertTrue(readFk.equals(testFk));
        assertTrue(readFk.getName().equalsIgnoreCase(testFk.getNewName()));
    }

    @After
    public void tearDown() throws SQLException {
        List<Table> tables = TABLE_DAO.readAll(getTestDatabase().getId());
        if (tables.contains(orders))
            TABLE_DAO.delete(orders.getId());
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
