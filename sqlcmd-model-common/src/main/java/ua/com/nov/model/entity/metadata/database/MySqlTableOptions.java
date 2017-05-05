package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.table.Table;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MySqlTableOptions extends MetaDataOptions<Table> {
    public static class Builder extends MetaDataOptions.Builder<MySqlTableOptions> {

        public Builder() {
            super(MySqlDb.class);
        }

        public Builder engine(String engine) {
            addOption("ENGINE", engine);
            return this;
        }

        public Builder comment(String engine) {
            addOption("COMMENT", engine);
            return this;
        }

        public Builder rowFormat(String rowFormat) {
            addOption("ROW_FORMAT ", rowFormat);
            return this;
        }

        public Builder defaultCharset(String defaultCharset) {
            addOption("DEFAULT CHARACTER SET", defaultCharset);
            return this;
        }

        public Builder collate(String collate) {
            addOption("COLLATE", collate);
            return this;
        }

        public Builder autoIncrement(Integer autoIncrement) {
            addOption("AUTO_INCREMENT", autoIncrement.toString());
            return this;
        }

        public Builder checkSum(Boolean checkSum) {
            addOption("CHECKSUM", checkSum.toString());
            return this;
        }

        public Builder avgRowLength (Integer avgRowLength) {
            addOption("AVG_ROW_LENGTH ", avgRowLength.toString());
            return this;
        }

        public Builder createOptions(String parameters) {
            addOption("", parameters);
            return this;
        }

        @Override
        public MySqlTableOptions build() {
            return new MySqlTableOptions(this);
        }
    }

    public MySqlTableOptions(Builder builder) {
        super(builder);
    }

    public String getEngine() {
        return getOption("ENGINE");
    }

    public String getRowFormat() {
        return getOption("ROW_FORMAT");
    }

    public String getDefaultCharset() {
        return getOption("DEFAULT CHARACTER SET");
    }

    public String getCollation() {
        return getOption("COLLATION");
    }

    public Integer getAutoIncrement() {
        return Integer.valueOf(getOption("AUTO_INCREMENT"));
    }

    public Boolean getCheckSum() {
        return Boolean.valueOf(getOption("CHECKSUM"));
    }

    public Integer getAvgRowLength() {
        return Integer.valueOf(getOption("AVG_ROW_LENGTH"));
    }

    public String getCreateOptions() {
        return getOption("");
    }

    @Override
    public String getCreateOptionsDefinition() {
        StringBuilder sb = new StringBuilder();
        String s = "";
        for (Map.Entry<String, String> entry : getOptionsMap().entrySet()) {
            if (entry.getKey().isEmpty())
                sb.append(s).append(entry.getValue());
            else
                sb.append(s).append(entry.getKey()).append("=").append(entry.getValue());
            s = " \n";
        }
        return sb.toString();
    }

    @Override
    public List<String> getUpdateOptionsDefinition() {
        List<String> result = new LinkedList<>();
        result.add(getCreateOptionsDefinition().replace(' ', ','));
        return result;
    }

}
