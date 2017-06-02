package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.statement.DataDefinitionSqlStmtSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.schema.Schema;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SchemaDao extends MetaDataDao<Schema.Id, Schema, Database.Id> {

    @Override
    protected DataDefinitionSqlStmtSource<Schema.Id, Schema, Database.Id> getSqlStmtSource(Database db) {
        return db.getDatabaseMdSqlStmtSource();
    }

    @Override
    protected AbstractRowMapper<Schema, Database.Id> getRowMapper(Database.Id id) {
        return new AbstractRowMapper<Schema, Database.Id>(id) {
            @Override
            public Schema mapRow(ResultSet rs, int i) throws SQLException {
                Schema.Id schemaId = new Schema.Id(id,
                        rs.getString("TABLE_CATALOG"), rs.getString("TABLE_SCHEM"));
                return new Schema(schemaId, null);
            }
        };
    }

    @Override
    protected ResultSet getResultSet(Database.Id dbId, String name) throws SQLException {
        return getDbMetaData().getSchemas(null, name);
    }

    @Override
    public void update(Schema value) throws DaoSystemException {
        throw new UnsupportedOperationException();
    }
}
