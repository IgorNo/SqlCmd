package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.datatype.DataType;
import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static ua.com.nov.model.entity.metadata.datatype.DataType.NOT_NULL;

public abstract class AbstractTableDaoTest {

    protected static Database testDb;
    protected static DataSource dataSource;

    protected static final TableDao TABLE_DAO = new TableDao();
    protected static final ColumnDao COLUMN_DAO = new ColumnDao();
    protected static final PrimaryKeyDao PRIMARY_KEY_DAO = new PrimaryKeyDao();
    protected static final ForeignKeyDao FOREIGN_KEY_DAO = new ForeignKeyDao();
    protected static final UniqueKeyDao UNIQUE_KEY_DAO = new UniqueKeyDao();
    protected static final IndexDao INDEX_DAO = new IndexDao();

    protected static Table customers, products, orders, users;

    protected static DataType integer;

    protected static void createTestData(String catalog, String schema, String aiTypeName) throws SQLException {
        testDb = new DatabaseDao().setDataSource(dataSource).read(testDb.getId());
        TABLE_DAO.setDataSource(dataSource);
        COLUMN_DAO.setDataSource(dataSource);
        PRIMARY_KEY_DAO.setDataSource(dataSource);
        FOREIGN_KEY_DAO.setDataSource(dataSource);
        UNIQUE_KEY_DAO.setDataSource(dataSource);
        INDEX_DAO.setDataSource(dataSource);

        DataType serial = testDb.getDataType(aiTypeName);
        integer = testDb.getMostApproximateDataTypes(JdbcDataTypes.INTEGER);
        DataType varchar = testDb.getMostApproximateDataTypes(JdbcDataTypes.VARCHAR);
        DataType text = testDb.getMostApproximateDataTypes(JdbcDataTypes.LONGVARCHAR);
        DataType numeric = testDb.getMostApproximateDataTypes(JdbcDataTypes.NUMERIC);
        DataType date = testDb.getMostApproximateDataTypes(JdbcDataTypes.DATE);

        Table.Id customersId = new Table.Id(testDb.getId(), "Customers", catalog, schema);
        customers = new Table.Builder(customersId)
                .addColumn(new Column.Builder("id", serial).autoIncrement(true).nullable(NOT_NULL))
                .addColumn(new Column.Builder("name", varchar).size(100).nullable(NOT_NULL))
                .addColumn(new Column.Builder("phone", varchar).size(20).defaultValue("0"))
                .addColumn(new Column.Builder("address", varchar).size(150))
                .addColumn(new Column.Builder("rating", integer))
                .addConstraint(new PrimaryKey.Builder("id"))
                .addConstraint(new UniqueKey.Builder("name", "phone"))
                .addIndex(new Index.Builder("address"))
                .build();

        Table.Id productsId = new Table.Id(testDb.getId(), "Products", catalog, schema);
        products = new Table.Builder(productsId)
                .addColumn(new Column.Builder("id", serial).autoIncrement(true).nullable(NOT_NULL))
                .addColumn(new Column.Builder("description", varchar).size(100).nullable(NOT_NULL))
                .addColumn(new Column.Builder("details", text))
                .addColumn(new Column.Builder("price", numeric).size(8).precision(0).defaultValue("0"))
                .addConstraint(new PrimaryKey.Builder("id"))
                .build();

        Table.Id ordersId = new Table.Id(testDb.getId(), "Orders", catalog, schema);
        orders = new Table.Builder(ordersId)
                .addColumn(new Column.Builder("id", serial).autoIncrement(true).nullable(NOT_NULL))
                .addColumn(new Column.Builder("date", date))
                .addColumn(new Column.Builder("product_id", integer).nullable(DataType.NOT_NULL))
                .addColumn(new Column.Builder("qty", integer))
                .addColumn(new Column.Builder("amount", numeric).size(10).precision(2))
                .addColumn(new Column.Builder("customer_id", integer))
                .addConstraint(new PrimaryKey.Builder("id"))
                .addConstraint(new ForeignKey.Builder("product_id", products.getColumn("id").getId())
                        .deleteRule(ForeignKey.Rule.NO_ACTION).updateRule(ForeignKey.Rule.NO_ACTION))
                .addConstraint(new ForeignKey.Builder("customer_id", customers.getColumn("id").getId())
                        .deleteRule(ForeignKey.Rule.NO_ACTION).updateRule(ForeignKey.Rule.NO_ACTION))
                .build();

        Table.Id usersId = new Table.Id(testDb.getId(), "Users", catalog, schema);
        users = new Table.Builder(usersId)
                .addColumn(new Column.Builder("login", varchar).size(25))
                .addColumn(new Column.Builder("password",varchar).size(25))
                .addConstraint(new PrimaryKey.Builder("login"))
                .build();
    }

    @Before
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
        assertTrue(customers.getUniqueKeyList().size() == table.getUniqueKeyList().size());
        for (UniqueKey key : customers.getUniqueKeyList()) {
            assertTrue(table.getUniqueKeyList().contains(key));
        }
        assertTrue(customers.getIndexList().size() == table.getIndexList().size());
        for (Index key : customers.getIndexList()) {
            assertTrue(table.getIndexList().contains(key));
        }

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
        List<Table> tables = TABLE_DAO.readAll(testDb.getId());
        assertTrue(tables.contains(customers));
        assertTrue(tables.contains(products));
        assertTrue(tables.contains(orders));
    }

    @Test
    public void testRenameTable() throws SQLException{
        customers.setNewName("table11");
        TABLE_DAO.update(customers);
        Table.Id updateTableId = new Table.Id(customers.getDb().getId(), customers.getNewName(), customers.getCatalog(),
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
        TABLE_DAO.deleteAll(testDb.getId());
        assertTrue(TABLE_DAO.readAll(testDb.getId()).size() == 0);
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
        Column readCol = COLUMN_DAO.read(new Column.Id(customers.getId(), "test"));
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
        assertTrue(readTable.getForeignKeyList().contains(fk));
    }

    @Test
    public void testRenameForeignKey() throws SQLException {
        ForeignKey testFk = orders.getForeignKeyList().get(0);
        testFk.setNewName("test");
        FOREIGN_KEY_DAO.update(testFk);
        ForeignKey readFk = FOREIGN_KEY_DAO.read(new ForeignKey.Id(orders.getId(), "test"));
        assertTrue(readFk.equals(testFk));
        assertTrue(readFk.getName().equalsIgnoreCase(testFk.getNewName()));
    }

    @Test
    public void testDeleteAddReadUniqueKey() throws SQLException {
        UniqueKey uk = customers.getUniqueKeyList().get(0);
        UNIQUE_KEY_DAO.delete(uk.getId());
        try {
            UNIQUE_KEY_DAO.read(uk.getId());
            assertTrue(false);
        } catch (IllegalArgumentException e) {/*NOP*/}
        UNIQUE_KEY_DAO.create(uk);
        Table readTable = TABLE_DAO.read(customers.getId());
        assertTrue(readTable.getUniqueKeyList().contains(uk));
    }

    @Test
    public void testRenameUniqueKey() throws SQLException {
        UniqueKey testUk = customers.getUniqueKeyList().get(0);
        testUk.setNewName("test");
        UNIQUE_KEY_DAO.update(testUk);
        UniqueKey readUk = UNIQUE_KEY_DAO.read(new UniqueKey.Id(customers.getId(), "test"));
        assertTrue(readUk.equals(testUk));
        assertTrue(readUk.getName().equalsIgnoreCase(testUk.getNewName()));
    }

    @Test
    public void testDeleteAddReadIndex() throws SQLException {
        Index index = customers.getIndexList().get(0);
        INDEX_DAO.delete(index.getId());
        try {
            INDEX_DAO.read(index.getId());
            assertTrue(false);
        } catch (IllegalArgumentException e) {/*NOP*/}
        INDEX_DAO.create(index);
        Table readTable = TABLE_DAO.read(customers.getId());
        assertTrue(readTable.getIndexList().contains(index));
    }


    @After
    public void tearDown() throws SQLException {
        List<Table> tables = TABLE_DAO.readAll(testDb.getId());
        if (tables.contains(orders))
            TABLE_DAO.delete(orders.getId());
        TABLE_DAO.deleteAll(testDb.getId());
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
