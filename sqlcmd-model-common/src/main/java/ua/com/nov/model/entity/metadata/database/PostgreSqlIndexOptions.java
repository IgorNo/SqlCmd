package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.table.Index;

public class PostgreSqlIndexOptions extends MetaDataOptions<Index> {

    public PostgreSqlIndexOptions(Builder builder) {
        super(builder);
    }

    public String getUsing() {
        return getOption("USING");
    }

    @Override
    public String getCreateOptionsDefinition() {
        return null;
    }
}
