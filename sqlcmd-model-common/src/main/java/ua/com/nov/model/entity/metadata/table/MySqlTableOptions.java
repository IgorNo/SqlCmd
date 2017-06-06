package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.server.MySqlServer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MySqlTableOptions extends MetaDataOptions<Table> {
    public static class Builder extends MetaDataOptions.Builder<MySqlTableOptions> {

        public Builder() {
            super(MySqlServer.class);
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
            addOption("ROW_FORMAT", rowFormat);
            return this;
        }

        public Builder defaultCharset(String defaultCharset) {
            addOption("DEFAULT CHARSET", defaultCharset);
            return this;
        }

        public Builder collate(String collate) {
            addOption("collate", collate);
            return this;
        }

        public Builder autoIncrement(Integer autoIncrement) {
            addOption("AUTO_INCREMENT", autoIncrement.toString());
            return this;
        }

        public Builder checkSum(boolean checkSum) {
            if (checkSum)
                addOption("CHECKSUM", "1");
            else
                addOption("CHECKSUM", "0");
            return this;
        }

        public Builder avgRowLength (Integer avgRowLength) {
            addOption("AVG_ROW_LENGTH", avgRowLength.toString());
            return this;
        }

        public Builder minRows(Integer minRows) {
            addOption("MIN_ROWS", minRows.toString());
            return this;
        }

        public Builder maxRows(Integer minRows) {
            addOption("MAX_ROWS", minRows.toString());
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
        return getOption("DEFAULT CHARSET");
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

    public String getMinRows() {
        return getOption("MIN_ROWS");
    }
    public String getMaxRows() {
        return getOption("MAX_ROWS");
    }

    public String getComment() {
        return getOption("COMMENT");
    }

    @Override
    public List<String> getUpdateOptionsDefinition() {
        List<String> result = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        String s = "";
        for (Map.Entry<String, String> entry : getOptionsMap().entrySet()) {
            sb.append(s).append(entry.getKey()).append(" = ").append(entry.getValue());
            s = " \n";
        }
        result.add(sb.toString());
        return result;
    }

}
