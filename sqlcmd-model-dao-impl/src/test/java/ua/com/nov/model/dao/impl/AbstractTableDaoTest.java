package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DaoBusinessLogicException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.entity.Hierarchical;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.MetaData;
import ua.com.nov.model.entity.metadata.MetaDataId;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.datatype.DataType;
import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.schema.Schema;
import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public abstract class AbstractTableDaoTest {

    protected static final MetaDataDao<Table.Id, Table, Schema.Id> TABLE_DAO = new TableDao();
    protected static final ColumnDao COLUMN_DAO = new ColumnDao();
    protected static final PrimaryKeyDao PRIMARY_KEY_DAO = new PrimaryKeyDao();
    protected static final ForeignKeyDao FOREIGN_KEY_DAO = new ForeignKeyDao();
    protected static final UniqueKeyDao UNIQUE_KEY_DAO = new UniqueKeyDao();
    protected static final IndexDao INDEX_DAO = new IndexDao();
    protected static Database testDb;
    protected static DataSource dataSource;
    protected static Table customers, products, orders, users, temp;
    protected static Schema.Id schemaId;

    protected static DataType integer;

    protected static void createTestData(String catalog, String schema, String aiTypeName, String tableType,
                                         MetaDataOptions<Table> options)
            throws DaoSystemException, DaoBusinessLogicException {
        testDb = new DatabaseDao().setDataSource(dataSource).read(testDb.getId());
        schemaId = new Schema.Id(testDb.getId(), catalog, schema);
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
        customers = new Table.Builder(customersId).viewName("Покупатели")
                .addColumn(new Column.Builder("id", serial).autoIncrement(true).nullable(DataType.NOT_NULL))
                .addColumn(new Column.Builder("name", varchar).size(100).nullable(DataType.NOT_NULL))
                .addColumn(new Column.Builder("phone", varchar).size(20).defaultValue("0"))
                .addColumn(new Column.Builder("address", varchar).size(150))
                .addColumn(new Column.Builder("rating", integer))
                .addConstraint(new PrimaryKey.Builder("id"))
                .addConstraint(new UniqueKey.Builder("name", "phone"))
                .addIndex(new Index.Builder("address"))
                .build();

        Table.Id productsId = new Table.Id(testDb.getId(), "Products", catalog, schema);
        products = new Table.Builder(productsId).viewName("Продукция")
                .addColumn(new Column.Builder("id", serial).autoIncrement(true).nullable(DataType.NOT_NULL))
                .addColumn(new Column.Builder("description", varchar).size(100).nullable(DataType.NOT_NULL))
                .addColumn(new Column.Builder("details", text))
                .addColumn(new Column.Builder("price", numeric).size(8).precision(0).defaultValue("0"))
                .addConstraint(new PrimaryKey.Builder("id"))
                .build();

        Table.Id ordersId = new Table.Id(testDb.getId(), "Orders", catalog, schema);
        orders = new Table.Builder(ordersId).viewName("Заказы")
                .addColumn(new Column.Builder("id", serial).autoIncrement(true).nullable(DataType.NOT_NULL))
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
        users = new Table.Builder(usersId).viewName("Пользователи")
                .addColumn(new Column.Builder("id", serial).autoIncrement(true).nullable(DataType.NOT_NULL))
                .addColumn(new Column.Builder("login", varchar).size(25).nullable(DataType.NOT_NULL))
                .addColumn(new Column.Builder("password", varchar).size(25))
                .addConstraint(new PrimaryKey.Builder("login"))
                .addConstraint(new UniqueKey.Builder("id"))
                .options(options)
                .build();

        Table.Id tempId = new Table.Id(testDb.getId(), "Temporary", catalog, schema);
        temp = new Table.Builder(tempId).viewName("Временная таблица").type(tableType)
                .addColumn(new Column.Builder("id", varchar).size(10))
                .build();
    }

    protected static void tearDownClass() throws SQLException {
        dataSource.getConnection().close();
    }

    protected static void compareOptions(Optional<?> options, Optional<?> rOptions) {
        for (Map.Entry<String, String> entry : options.getOptionsMap().entrySet()) {
            assertTrue(entry.getValue().equalsIgnoreCase(rOptions.getOption(entry.getKey())));
        }
    }

    @Before
    public void setUp() throws DaoSystemException, DaoBusinessLogicException {
        tearDown();
        TABLE_DAO.create(customers);
        TABLE_DAO.create(products);
        TABLE_DAO.create(orders);
        TABLE_DAO.create(users);
//        TABLE_DAO.create(temp);
    }

    @Test
    public void testReadTableMetaData() throws DaoSystemException, DaoBusinessLogicException {
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
        assertTrue(customers.getViewName().equalsIgnoreCase(table.getViewName()));

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

        Table result = TABLE_DAO.read(users.getId());
        assertTrue(users.equals(result));
        compareColumns(result, users);
        if (getCreateOptions() != null)
            compareOptions(getCreateOptions(), result.getOptions());
    }

    protected abstract Optional<?> getCreateOptions();

    @Test
    public void testUpdateTable() throws DaoSystemException, DaoBusinessLogicException {
        Table table = new Table.Builder(users.getId()).viewName("New Comment").options(getUpdateOptions()).build();
        TABLE_DAO.update(table);
        Table result = TABLE_DAO.read(users.getId());
        assertTrue(result.getViewName().equalsIgnoreCase(table.getViewName()));
        if (getUpdateOptions() != null)
            compareOptions(getUpdateOptions(), result.getOptions());
    }

    protected abstract Optional<Table> getUpdateOptions();

    protected void compareColumns(Table table1, Table table2) {
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
            assertTrue(col.isNotNull() == testCol.isNotNull());
            assertTrue(col.isAutoIncrement() == testCol.isAutoIncrement());
            if (testCol.getViewName() != null)
                assertTrue(testCol.getViewName().equals(col.getViewName()));
        }
    }

    @Test
    public void testCreateTemporaryTable() throws DaoSystemException {
        TABLE_DAO.create(temp);
        Table readTable = TABLE_DAO.read(temp.getId());
        assertTrue(readTable.equals(temp));
    }

    @Test
    public void testReadAllTables() throws DaoSystemException {
        List<Table> tables = TABLE_DAO.readAll(schemaId);
        assertTrue(tables.contains(customers));
        assertTrue(tables.contains(products));
        assertTrue(tables.contains(orders));
        assertTrue(tables.contains(users));
    }

    @Test(expected = DaoBusinessLogicException.class)
    public void testDeleteTable() throws DaoSystemException, DaoBusinessLogicException {
        TABLE_DAO.delete(orders);
        TABLE_DAO.read(orders.getId());
        assertTrue(false);
    }

    @Test
    public void testRenameTable() throws DaoSystemException {
        renameMetaData(TABLE_DAO, users, new Table.Id(users.getId().getContainerId(), "new_name"));
    }

    private <I extends MetaDataId<C>, E extends MetaData<I>, C extends Hierarchical>
    void renameMetaData(MetaDataDao<I, E, C> dao, E entity, I updatedId)
            throws DaoSystemException {
        dao.rename(entity, updatedId.getName());
        E result = dao.read(updatedId);
        assertTrue(result.getName().equalsIgnoreCase(updatedId.getName()));
    }

    @Test
    public void testRenameColumn() throws DaoSystemException {
        renameMetaData(COLUMN_DAO, customers.getColumn("name"), new Column.Id(customers.getId(), "new_name"));
    }

    @Test
    public void testRenamePrimaryKey() throws DaoSystemException {
        renameMetaData(PRIMARY_KEY_DAO, customers.getPrimaryKey(), new PrimaryKey.Id(customers.getId(), "new_name"));
    }

    @Test
    public void testRenameForeignKey() throws DaoSystemException, DaoBusinessLogicException {
        renameMetaData(FOREIGN_KEY_DAO, orders.getForeignKeyList().get(0), new ForeignKey.Id(orders.getId(), "new_name"));
    }

    @Test
    public void testRenameUniqueKey() throws DaoSystemException, DaoBusinessLogicException {
        renameMetaData(UNIQUE_KEY_DAO, customers.getUniqueKeyList().get(0), new UniqueKey.Id(customers.getId(), "new_name"));
    }

    @Test
    public void testAddColumn() throws DaoSystemException, DaoBusinessLogicException {
        Column testCol = new Column.Builder(customers.getId(), "test", integer).build();
        COLUMN_DAO.create(testCol);
        Column readCol = COLUMN_DAO.read(testCol.getId());
        assertTrue(testCol.equals(readCol));
    }

    @Test
    public void testReadColumn() throws DaoSystemException {
        Column testCol = customers.getColumn("name");
        Column readCol = COLUMN_DAO.read(testCol.getId());
        assertTrue(testCol.equals(readCol));
    }

    @Test(expected = DaoBusinessLogicException.class)
    public void testDeleteColumn() throws DaoSystemException {
        COLUMN_DAO.delete(customers.getColumn("address"));
        COLUMN_DAO.read(customers.getColumn("address").getId());
        assertTrue(false);
    }

    @Test
    public void testReadDeleteAddPrimaryKey() throws DaoSystemException, DaoBusinessLogicException {
        Table testTable = TABLE_DAO.read(users.getId());
        readDeleteAddMetaData(PRIMARY_KEY_DAO, testTable.getPrimaryKey().getId(), users.getPrimaryKey());
    }

    protected <I extends MetaDataId<C>, E extends MetaData<I>, C extends Hierarchical>
    void readDeleteAddMetaData(MetaDataDao<I, E, C> dao, I mdId, E newMd)
            throws DaoSystemException {
        E md = dao.read(mdId);
        assertTrue(md.getId().equals(mdId));
        dao.delete(md);
        try {
            dao.read(mdId);
            assertTrue(false);
        } catch (DaoBusinessLogicException e) {/*NOP*/}
        dao.create(newMd);
        E result = dao.read(newMd.getId());
        assertTrue(result.equals(md));
    }

    @Test
    public void testReadDeleteAddForeignKey() throws DaoSystemException {
        Table testTable = TABLE_DAO.read(orders.getId());
        ForeignKey fk = orders.getForeignKeyList().get(0);
        readDeleteAddMetaData(FOREIGN_KEY_DAO, testTable.getForeignKey(fk.getName()).getId(), fk);
    }

    @Test
    public void testReadDeleteAddUniqueKey() throws DaoSystemException {
        Table testTable = TABLE_DAO.read(customers.getId());
        UniqueKey uk = customers.getUniqueKeyList().get(0);
        readDeleteAddMetaData(UNIQUE_KEY_DAO, testTable.getUniqueKey(uk.getName()).getId(), uk);
    }

    @Test
    public void testDeleteAddReadIndex() throws DaoSystemException {
        Table testTable = TABLE_DAO.read(customers.getId());
        Index index = customers.getIndexList().get(0);
        readDeleteAddMetaData(INDEX_DAO, testTable.getIndex(index.getName()).getId(), index);
    }

    @After
    public void tearDown() throws DaoSystemException, DaoBusinessLogicException {
        List<Table> tables = TABLE_DAO.readAll(schemaId);
        if (tables.contains(orders)) {
            TABLE_DAO.delete(orders);
            tables.remove(orders);
        }
        for (Table table : tables) {
            TABLE_DAO.delete(table);
        }
    }
}
