package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DaoBusinessLogicException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.schema.Schema;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public abstract class AbstractSchemaDaoTest {
    protected static final SchemaDao SCHEMA_DAO = new SchemaDao();
    protected static Database testDb;
    protected static DataSource dataSource;
    protected static Schema.Id schemaId;
    protected static Schema schema;

    protected static void createTestData(String catalog, Optional<Schema> options)
            throws DaoSystemException {
        schemaId = new Schema.Id(testDb.getId(), catalog, "tmp_schema");
        schema = new Schema(schemaId, options);
        SCHEMA_DAO.setDataSource(dataSource);
    }

    protected static void tearDownClass() throws SQLException {
        dataSource.getConnection().close();
    }

    @Before
    public void setUp() throws DaoSystemException {
        tearDown();
        SCHEMA_DAO.create(schema);
    }

    @Test
    public void testReadSchema() throws DaoSystemException {
        Schema result = SCHEMA_DAO.read(schemaId);
        assertTrue(result.equals(schema));
    }

    @Test
    public void testReadAllSchemas() throws DaoSystemException {
        List<Schema> schemas = SCHEMA_DAO.readAll(testDb.getId());
        assertTrue(schemas.size() > 1);
        assertTrue(schemas.contains(schema));
    }

    @Test(expected = DaoBusinessLogicException.class)
    public void testDeleteSchema() throws DaoSystemException {
        SCHEMA_DAO.delete(schema);
        SCHEMA_DAO.read(schemaId);
        assertTrue(false);
    }

    @Test
    public void testRenameSchema() throws DaoSystemException {
        Schema.Id updatedId = new Schema.Id(testDb.getId(), schema.getId().getCatalog(), "new_name");
        SCHEMA_DAO.rename(schema, updatedId.getName());
        Schema result = SCHEMA_DAO.read(updatedId);
        assertTrue(result.getName().equalsIgnoreCase(updatedId.getName()));
    }


    @After
    public void tearDown() throws DaoSystemException {
        SCHEMA_DAO.deleteIfExist(schema);
    }

}
