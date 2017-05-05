package ua.com.nov.model.entity.metadata.schema;

import ua.com.nov.model.entity.Schematic;
import ua.com.nov.model.entity.metadata.MetaData;
import ua.com.nov.model.entity.metadata.MetaDataId;

public class SchemaMd<K extends SchemaMd.Id> extends MetaData<K> {

    public SchemaMd(K id, String type) {
        super(id, type);
    }

    public String getCatalog() {
        return getId().getCatalog();
    }

    public String getSchema() {
        return getId().getSchema();
    }


    public abstract static class Id extends MetaDataId<Schema.Id> implements Schematic {
        public Id(Schema.Id schemaId, String name) {
            super(schemaId, name);
        }

        @Override
        public String getCatalog() {
            return getContainerId().getCatalog();
        }

        @Override
        public String getSchema() {
            return getContainerId().getSchema();
        }

    }

}
