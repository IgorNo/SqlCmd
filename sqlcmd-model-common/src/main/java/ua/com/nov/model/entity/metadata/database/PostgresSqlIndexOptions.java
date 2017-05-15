package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.table.Index;

public class PostgresSqlIndexOptions extends MetaDataOptions<Index> {

    public PostgresSqlIndexOptions(Builder builder) {
        super(builder);
    }

    public String getUsing() {
        return getOption("USING");
    }
}
