package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.MdCreateOptions;
import ua.com.nov.model.entity.MetaDataOptions;

public class HyperSqlCreateTableOptions extends TableOptions implements MdCreateOptions {

    public static class Builder extends MetaDataOptions.Builder {


        @Override
        public Object build() {
            return new HyperSqlCreateTableOptions(this);
        }
    }

    public HyperSqlCreateTableOptions(Builder builder) {
        super(builder);
    }
}
