package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.dao.exception.BusinessLogicException;
import ua.com.nov.model.dao.exception.DAOSystemException;
import ua.com.nov.model.entity.MetaDataId;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.AbstractMetaData;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.datatype.DataTypes;
import ua.com.nov.model.entity.metadata.datatype.DbDataType;
import ua.com.nov.model.entity.metadata.schema.Schema;
import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.column.ColumnOptions;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public abstract class AbstractTableDaoTest {

    protected static final SchemaDao SCHEMA_DAO = new SchemaDao();
    protected static final MetaDataDao<Table.Id, Table, Schema.Id> TABLE_DAO = new TableDao();
    protected static final ColumnDao COLUMN_DAO = new ColumnDao();
    protected static final PrimaryKeyDao PRIMARY_KEY_DAO = new PrimaryKeyDao();
    protected static final ForeignKeyDao FOREIGN_KEY_DAO = new ForeignKeyDao();
    protected static final UniqueKeyDao UNIQUE_KEY_DAO = new UniqueKeyDao();
    protected static final IndexDao INDEX_DAO = new IndexDao();
    protected static Database testDb;
    protected static DataSource dataSource;
    public static Table customers, products, orders, users, temp;
    protected static Schema testSchema;

    protected static MetaDataOptions<Table> tableOptions;
    protected static ColumnOptions.Builder numberColumnOptions;
    protected static ColumnOptions.Builder charColumnOptions;
    protected static ColumnOptions.Builder generatedColumnOptions;

    protected static DbDataType integer, character;


    protected static void createTestData(String catalog, String schema, String aiTypeName, String tableType,
                                         Optional<Schema> options)
            throws DAOSystemException, BusinessLogicException {
        testDb = new DatabaseDao(dataSource).read(testDb.getId());
        SCHEMA_DAO.setDataSource(dataSource);
        TABLE_DAO.setDataSource(dataSource);
        COLUMN_DAO.setDataSource(dataSource);
        PRIMARY_KEY_DAO.setDataSource(dataSource);
        FOREIGN_KEY_DAO.setDataSource(dataSource);
        UNIQUE_KEY_DAO.setDataSource(dataSource);
        INDEX_DAO.setDataSource(dataSource);

        Schema.Id schemaId = new Schema.Id(testDb.getId(), catalog, "tmp_schema");
        testSchema = new Schema(schemaId, options);

        DbDataType serial = testDb.getServer().getDataType(aiTypeName);
        integer = testDb.getServer().getMostApproximateDataTypes(DataTypes.BIGINT);
        character = testDb.getServer().getMostApproximateDataTypes(DataTypes.CHAR);
        DbDataType varchar = testDb.getServer().getMostApproximateDataTypes(DataTypes.VARCHAR);
        DbDataType text = testDb.getServer().getMostApproximateDataTypes(DataTypes.LONGVARCHAR);
        DbDataType numeric = testDb.getServer().getMostApproximateDataTypes(DataTypes.NUMERIC);
        DbDataType date = testDb.getServer().getMostApproximateDataTypes(DataTypes.DATE);
        DbDataType time = testDb.getServer().getMostApproximateDataTypes(DataTypes.TIME);


        Table.Id customersId = new Table.Id(testDb.getId(), "Customers", catalog, schema);
        customers = new Table.Builder(customersId).viewName("Покупатели")
                .addColumn(new Column.Builder("id", serial).primaryKey().autoIncrement())
                .addColumn(new Column.Builder("name", varchar).size(100).notNull())
                .addColumn(new Column.Builder("phone", varchar).size(20).defaultValue("0"))
                .addColumn(new Column.Builder("address", varchar).size(150))
                .addColumn(new Column.Builder("rating", integer))
                .addConstraint(new UniqueKey.Builder("name", "phone"))
                .addIndex(new Index.Builder("address"))
                .build();

        Table.Id productsId = new Table.Id(testDb.getId(), "Products", catalog, schema);
        products = new Table.Builder(productsId).viewName("Продукция")
                .addColumn(new Column.Builder("id", serial).autoIncrement(true).primaryKey())
                .addColumn(new Column.Builder("description", varchar).size(100).notNull())
                .addColumn(new Column.Builder("details", text))
                .addColumn(new Column.Builder("price", numeric).size(8).precision(0).defaultValue("0"))
                .build();

        Table.Id ordersId = new Table.Id(testDb.getId(), "Orders", catalog, schema);
        orders = new Table.Builder(ordersId).viewName("Заказы")
                .addColumn(new Column.Builder("id", serial).autoIncrement().nullable(DbDataType.NOT_NULL))
                .addColumn(new Column.Builder("date", date))
                .addColumn(new Column.Builder("product_id", integer).notNull()
                .references(products.getColumn("id").getId(),
                        ForeignKey.Rule.NO_ACTION, ForeignKey.Rule.NO_ACTION))
                .addColumn(new Column.Builder("qty", integer))
                .addColumn(new Column.Builder("amount", numeric).size(10).precision(2))
                .addColumn(new Column.Builder("customer_id", integer))
                .addConstraint(new PrimaryKey.Builder("id"))
                .addConstraint(new ForeignKey.Builder("customer_id", customers.getColumn("id").getId())
                        .deleteRule(ForeignKey.Rule.NO_ACTION).updateRule(ForeignKey.Rule.NO_ACTION))
                .build();

        Table.Id usersId = new Table.Id(testDb.getId(), "Users", catalog, schema);
        users = new Table.Builder(usersId).viewName("Пользователи")
                .addColumn(new Column.Builder("id", serial).unique().options(numberColumnOptions))
                .addColumn(new Column.Builder("login", varchar).size(25).notNull().viewName("Логин"))
                .addColumn(new Column.Builder("password", varchar).size(25).notNull().options(charColumnOptions).viewName("Пароль"))
                .addColumn(new Column.Builder("login_", varchar).size(50).options(generatedColumnOptions))
                .addConstraint(new PrimaryKey.Builder("login", "password"))
                .options(tableOptions)
                .build();

        Table.Id tempId = new Table.Id(testDb.getId(), "Temporary", catalog, schema);
        temp = new Table.Builder(tempId).viewName("Временная таблица").type(tableType)
                .addColumn(new Column.Builder("id", varchar).size(10))
                .build();
    }

    protected static void tearDownClass() throws SQLException, DAOSystemException {
        dataSource.getConnection().close();
    }

    protected static void compareOptions(Optional<?> options1, Optional<?> options2) {
        for (Map.Entry<String, String> entry : options1.getOptionsMap().entrySet()) {
            String option1 = entry.getValue().replace("'", "");
            String option2 = options2.getOption(entry.getKey()).replace("'", "");
            if (!entry.getValue().contains("nextval"))
                assertTrue(option1.equalsIgnoreCase(option2));
        }
    }

    @Before
    public void setUp() throws DAOSystemException, BusinessLogicException {
        tearDown();
        SCHEMA_DAO.create(testSchema);
        TABLE_DAO.create(customers);
        TABLE_DAO.create(products);
        TABLE_DAO.create(orders);
        TABLE_DAO.create(users);
    }

    @Test
    public void testReadTableMetaData() throws DAOSystemException {
        Table table = TABLE_DAO.read(customers.getId());
        assertTrue(table.equals(customers));
        assertTrue(table.getPrimaryKey().equals(customers.getPrimaryKey()));
        assertTrue(table.getColumns().size() == customers.getColumns().size());
        compareAllColumns(customers, table);
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
        compareAllColumns(products, table);

        table = TABLE_DAO.read(orders.getId());
        assertTrue(table.equals(orders));
        compareAllColumns(orders, table);
        assertTrue(table.getColumns().size() == orders.getColumns().size());
        assertTrue(orders.getForeignKeyList().size() == table.getForeignKeyList().size());
        for (ForeignKey key : orders.getForeignKeyList()) {
            assertTrue(table.getForeignKeyList().contains(key));
        }

        Table result = TABLE_DAO.read(users.getId());
        assertTrue(users.equals(result));
        compareAllColumns(users, result);
        if (users.getOptions() != null)
            compareOptions(users.getOptions(), result.getOptions());
    }


    @Test
    public void testUpdateTable() throws DAOSystemException, BusinessLogicException {
        Table table = new Table.Builder(users.getId()).viewName("New Comment").options(getUpdateTableOptions()).build();
        TABLE_DAO.update(table);
        Table result = TABLE_DAO.read(users.getId());
        assertTrue(result.getViewName().equalsIgnoreCase(table.getViewName()));
        if (getUpdateTableOptions() != null)
            compareOptions(getUpdateTableOptions(), result.getOptions());
    }

    protected abstract Optional<Table> getUpdateTableOptions();

    protected void compareAllColumns(Table table1, Table table2) {
        for (Column col : table1.getColumns()) {
            Column testCol = table2.getColumn(col.getName());
            compareColumns(col, testCol);
        }
    }

    private void compareColumns(Column column1, Column column2) {
        assertTrue(column1.equals(column2));
        if (!column2.getName().equalsIgnoreCase("id") && !column2.getName().equalsIgnoreCase("details")) {
            if (!"bpchar".equals(column2.getDataType().getTypeName()))
                assertTrue(column2.getDataType().equals(column1.getDataType()));
            else
                assertTrue(column2.getDataType().getSqlType() == column1.getDataType().getSqlType());
        }
        if (column1.getColumnSize() != null) {
            assertTrue(column2.getColumnSize().equals(column1.getColumnSize()));
        }
        if (column1.getPrecision() != null) {
            assertTrue(column2.getPrecision().equals(column1.getPrecision()));
        }
        assertTrue(column1.isNotNull() == column2.isNotNull());
        assertTrue(column1.isAutoIncrement() == column2.isAutoIncrement());
        if (column1.getViewName() != null)
            assertTrue(column1.getViewName().equals(column2.getViewName()));
        if (column1.getOptions() != null)
            compareOptions(column1.getOptions(), column2.getOptions());
    }

    @Test
    public void testReadSchema() throws DAOSystemException {
        Schema result = SCHEMA_DAO.read(testSchema.getId());
        assertTrue(result.equals(testSchema));
    }

    @Test
    public void testReadAllSchemas() throws DAOSystemException {
        List<Schema> schemas = SCHEMA_DAO.readAll(testDb.getId());
        assertTrue(schemas.size() > 1);
        assertTrue(schemas.contains(testSchema));
    }

    @Test(expected = BusinessLogicException.class)
    public void testDeleteSchema() throws DAOSystemException {
        SCHEMA_DAO.delete(testSchema.getId());
        SCHEMA_DAO.read(testSchema.getId());
        assertTrue(false);
    }

    @Test
    public void testRenameSchema() throws DAOSystemException {
        Schema.Id updatedId = new Schema.Id(testDb.getId(), testSchema.getId().getCatalog(), "new_name");
        SCHEMA_DAO.rename(testSchema, updatedId.getName());
        Schema result = SCHEMA_DAO.read(updatedId);
        assertTrue(result.getName().equalsIgnoreCase(updatedId.getName()));
        SCHEMA_DAO.delete(result.getId());
    }

    @Test
    public void testCreateTemporaryTable() throws DAOSystemException {
        TABLE_DAO.create(temp);
        Table readTable = TABLE_DAO.read(temp.getId());
        assertTrue(readTable.equals(temp));
    }

    @Test
    public void testReadAllTables() throws DAOSystemException {
        List<Table> tables = TABLE_DAO.readAll(customers.getId().getContainerId());
        assertTrue(tables.contains(customers));
        assertTrue(tables.contains(products));
        assertTrue(tables.contains(orders));
        assertTrue(tables.contains(users));
    }

    @Test(expected = BusinessLogicException.class)
    public void testDeleteTable() throws DAOSystemException, BusinessLogicException {
        TABLE_DAO.delete(orders.getId());
        TABLE_DAO.read(orders.getId());
        assertTrue(false);
    }

    @Test
    public void testRenameTable() throws DAOSystemException {
        renameMetaData(TABLE_DAO, users, new Table.Id(users.getId().getContainerId(), "new_name"));
    }

    private <I extends AbstractMetaData.Id<C>, E extends AbstractMetaData<I>, C extends MetaDataId>
    void renameMetaData(MetaDataDao<I, E, C> dao, E entity, I updatedId)
            throws DAOSystemException {
        dao.rename(entity, updatedId.getName());
        E result = dao.read(updatedId);
        assertTrue(result.getName().equalsIgnoreCase(updatedId.getName()));
    }

    @Test
    public void testRenameColumn() throws DAOSystemException {
        renameMetaData(COLUMN_DAO, customers.getColumn("name"), new Column.Id(customers.getId(), "new_name"));
    }

    @Test
    public void testRenamePrimaryKey() throws DAOSystemException {
        renameMetaData(PRIMARY_KEY_DAO, customers.getPrimaryKey(), new PrimaryKey.Id(customers.getId(), "new_name"));
    }

    @Test
    public void testRenameForeignKey() throws DAOSystemException, BusinessLogicException {
        renameMetaData(FOREIGN_KEY_DAO, orders.getForeignKeyList().get(0), new ForeignKey.Id(orders.getId(), "new_name"));
    }

    @Test
    public void testRenameUniqueKey() throws DAOSystemException, BusinessLogicException {
        renameMetaData(UNIQUE_KEY_DAO, customers.getUniqueKeyList().get(0), new UniqueKey.Id(customers.getId(), "new_name"));
    }

    @Test
    public void testAddColumn() throws DAOSystemException, BusinessLogicException {
        Column testCol = new Column.Builder(customers.getId(), "test", integer).build();
        COLUMN_DAO.create(testCol);
        Column readCol = COLUMN_DAO.read(testCol.getId());
        assertTrue(testCol.equals(readCol));
    }

    @Test
    public void testReadColumn() throws DAOSystemException {
        Column testCol = customers.getColumn("name");
        Column readCol = COLUMN_DAO.read(testCol.getId());
        assertTrue(testCol.equals(readCol));
    }

    @Test
    public void testUpdateColumn() throws DAOSystemException, BusinessLogicException {
        Column col = new Column.Builder(users.getColumn("password").getId(), character).size(20)
                .viewName("Changed column").notNull().options(getUpdateColumnOptions()).build();
        COLUMN_DAO.update(col);
        Column result = COLUMN_DAO.read(col.getId());
        compareColumns(col, result);
    }

    protected abstract ColumnOptions.Builder<? extends ColumnOptions> getUpdateColumnOptions();

    @Test(expected = BusinessLogicException.class)
    public void testDeleteColumn() throws DAOSystemException {
        COLUMN_DAO.delete(customers.getColumn("address").getId());
        COLUMN_DAO.read(customers.getColumn("address").getId());
        assertTrue(false);
    }

    @Test
    public void testReadDeleteAddPrimaryKey() throws DAOSystemException, BusinessLogicException {
        Table testTable = TABLE_DAO.read(users.getId());
        readDeleteAddMetaData(PRIMARY_KEY_DAO, testTable.getPrimaryKey().getId(), users.getPrimaryKey());
    }

    protected <I extends AbstractMetaData.Id<C>, E extends AbstractMetaData<I>, C extends MetaDataId>
    void readDeleteAddMetaData(MetaDataDao<I, E, C> dao, I mdId, E newMd)
            throws DAOSystemException {
        E md = dao.read(mdId);
        assertTrue(md.getId().equals(mdId));
        dao.delete(md.getId());
        try {
            dao.read(mdId);
            assertTrue(false);
        } catch (BusinessLogicException e) {/*NOP*/}
        dao.create(newMd);
        E result = dao.read(newMd.getId());
        assertTrue(result.equals(md));
    }

    @Test
    public void testReadDeleteAddForeignKey() throws DAOSystemException {
        Table testTable = TABLE_DAO.read(orders.getId());
        ForeignKey fk = orders.getForeignKeyList().get(0);
        readDeleteAddMetaData(FOREIGN_KEY_DAO, testTable.getForeignKey(fk.getName()).getId(), fk);
    }

    @Test
    public void testReadDeleteAddUniqueKey() throws DAOSystemException {
        Table testTable = TABLE_DAO.read(customers.getId());
        UniqueKey uk = customers.getUniqueKeyList().get(0);
        readDeleteAddMetaData(UNIQUE_KEY_DAO, testTable.getUniqueKey(uk.getName()).getId(), uk);
    }

    @Test
    public void testDeleteAddReadIndex() throws DAOSystemException {
        Table testTable = TABLE_DAO.read(customers.getId());
        Index index = customers.getIndexList().get(0);
        readDeleteAddMetaData(INDEX_DAO, testTable.getIndex(index.getName()).getId(), index);
    }

    @After
    public void tearDown() throws DAOSystemException, BusinessLogicException {
        SCHEMA_DAO.deleteIfExist(testSchema.getId());
        List<Table> tables = TABLE_DAO.readAll(customers.getId().getContainerId());
        if (tables.contains(orders)) {
            TABLE_DAO.delete(orders.getId());
            tables.remove(orders);
        }
        for (Table table : tables) {
            TABLE_DAO.delete(table.getId());
        }
    }
}
