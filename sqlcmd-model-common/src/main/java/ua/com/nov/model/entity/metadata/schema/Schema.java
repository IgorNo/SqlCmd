package ua.com.nov.model.entity.metadata.schema;

import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.Schematic;
import ua.com.nov.model.entity.metadata.MetaData;
import ua.com.nov.model.entity.metadata.MetaDataId;
import ua.com.nov.model.entity.metadata.database.Database;

public class Schema extends MetaData<Schema.Id> {

    public Schema(Id id, Optional<Schema> options) {
        super(id, null, options);
    }

    @Override
    public String getCreateStmtDefinition(String conflictOption) {
        return String.format(super.getCreateStmtDefinition(conflictOption), "");
    }

    public static class Id extends MetaDataId<Database.Id> implements Schematic {
        private final String catalog;

        public Id(Database.Id dbId, String catalog, String schema) {
            super(dbId, schema);
            this.catalog = catalog;
        }

        public Id(Database.Id dbId, String catalog) {
            this(dbId, catalog, null);
        }

        @Override
        public String getMdName() {
            return "SCHEMA";
        }

        @Override
        public String getFullName() {
            StringBuilder sb = new StringBuilder();
            if (catalog != null) sb.append(catalog);
            if (getSchema() != null) {
                if (catalog != null) sb.append('.');
                sb.append(getSchema());
            }
            return sb.toString();
        }

        @Override
        public String getCatalog() {
            return getServer().convert(catalog);
        }

        @Override
        public String getSchema() {
            return getServer().convert(getName());
        }
    }

}
