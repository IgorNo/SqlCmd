package ua.com.nov.model.dao.fetch;

import ua.com.nov.model.entity.metadata.database.Database;

public final class TableFetchParameters extends FetchParametersSource<Database.Id> {

    public TableFetchParameters(Database.Id id, String catalog, String schemaPattern, String tableNamePattern,
                                String... types) {
        super(id);
        addParameter("catalog", catalog);
        addParameter("schemaPattern", schemaPattern);
        addParameter("tableNamePattern", tableNamePattern);
        addParameter("arrayTableTypes", types);
    }

    public String getCatalog() {
        return (String) getParameter("catalog");
    }

    public String getSchemaPattern() {
        return (String) getParameter("schemaPattern");
    }

    public String getTableNamePattern() {
        return (String) getParameter("tableNamePattern");
    }

    public String[] getTypes() {
        return (String[]) getParameter("arrayTableTypes");
    }
}
