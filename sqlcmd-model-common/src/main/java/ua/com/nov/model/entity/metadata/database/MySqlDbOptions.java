package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.server.MySqlServer;
import ua.com.nov.model.util.CollectionUtils;

public class MySqlDbOptions extends MetaDataOptions<Database> {

    public MySqlDbOptions(Builder builder) {
        super(builder);
    }

    public String getCharacterSet() {
        return getOption("CHARACTER SET");
    }

    public String getCollate() {
        return getOption("COLLATE");
    }

    @Override
    public String getCreateOptionsDefinition() {
        return CollectionUtils.toString(getOptionsMap(), "\n", " = ");
    }

    public static class Builder extends MetaDataOptions.Builder<MySqlDbOptions> {
        public Builder() {
            super(MySqlServer.class);
        }

        public Builder characterSet(String characterSet) {
            addOption("CHARACTER SET", characterSet);
            return this;
        }

        public Builder collate(String collate) {
            addOption("COLLATE", collate);
            return this;
        }

        @Override
        public MySqlDbOptions build() {
            return new MySqlDbOptions(this);
        }
    }
}
