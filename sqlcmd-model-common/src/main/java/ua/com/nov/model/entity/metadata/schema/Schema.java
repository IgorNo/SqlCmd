package ua.com.nov.model.entity.metadata.schema;

import ua.com.nov.model.entity.Schematic;
import ua.com.nov.model.entity.metadata.MetaData;
import ua.com.nov.model.entity.metadata.MetaDataId;
import ua.com.nov.model.entity.metadata.database.Database;

public class Schema extends MetaData<Schema.Id> {

    public static class Id extends MetaDataId<Database.Id> implements Schematic{
        private final String catalog;

        public Id(Database.Id containerId, String catalog, String schema) {
            super(containerId, schema);
            this.catalog = catalog;
        }

        public Id(Database.Id db, String name) {
            this(db, name, null);
        }

        @Override
        public String getMdName() {
            return "SHEMA";
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
            return getDb().convert(catalog);
        }

        @Override
        public String getSchema() {
            return getDb().convert(getName());
        }
    }

    public Schema(Id id) {
        super(id, null);
    }

}
