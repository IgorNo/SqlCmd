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
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static ua.com.nov.model.entity.metadata.datatype.DataType.*;

public abstract class AbstractTableDaoTest {

    public static final AbstractDao<TableId, Table, Database.DbId> DAO = new TableDao();

    private static DataSource dataSource;

    private static TableId customersId, productsId, ordersId;
    private static Table customers, products, orders;

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
        DataType date = testDb.getMostApproximateDataTypes(JdbcDataTypes.DATE);

        customersId = new TableId(testDb.getId(), "Customers", catalog, schema);
        customers = new Table.Builder(customersId)
                .addColumn(new Column.Builder("id", serial).autoIncrement(true))
                .addColumn(new Column.Builder("name", varchar).size(100).nullable(NOT_NULL))
                .addColumn(new Column.Builder("phone", varchar).size(20).defaultValue(""))
                .addColumn(new Column.Builder("address", varchar).size(150))
                .addColumn(new Column.Builder("rating", integer))
                .primaryKey(new PrimaryKey.Builder("id"))
                .addUniqueKey(new UniqueKey.Builder("name", "phone"))
                .build();

        productsId = new TableId(testDb.getId(), "Products", catalog, schema);
        products = new Table.Builder(productsId)
                .addColumn(new Column.Builder("id", serial).autoIncrement(true))
                .addColumn(new Column.Builder("description", varchar).size(100).nullable(NOT_NULL))
                .addColumn(new Column.Builder("price", numeric).size(8).precision(2).defaultValue("0"))
                .primaryKey(new PrimaryKey.Builder("id"))
                .build();

        ordersId = new TableId(testDb.getId(), "Orders", catalog, schema);
        orders = new Table.Builder(ordersId)
                .addColumn(new Column.Builder("id", serial).autoIncrement(true))
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
        DAO.create(customers);
        DAO.create(products);
        DAO.create(orders);
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
        assertTrue(tables.contains(orders));
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
