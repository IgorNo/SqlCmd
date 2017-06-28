package ua.com.nov.model.entity.metadata.schema;

import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.Schematic;
import ua.com.nov.model.entity.metadata.AbstractMetaData;

public class SchemaMd<K extends SchemaMd.Id> extends AbstractMetaData<K> {

    public SchemaMd(K id, String type, Optional<? extends SchemaMd> options) {
        super(id, type, options);
    }

    public String getCatalog() {
        return getId().getCatalog();
    }

    public String getSchema() {
        return getId().getSchema();
    }


    public abstract static class Id extends AbstractMetaData.Id<Schema.Id> implements Schematic {
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
