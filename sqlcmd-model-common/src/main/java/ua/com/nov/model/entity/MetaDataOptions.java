package ua.com.nov.model.entity;

import ua.com.nov.model.entity.metadata.database.Database;

import java.util.*;

public abstract class MetaDataOptions<E> implements Optional<E> {
    private final Class<? extends Database> dbClass;
    private final Map<String, String> optionsMap;

    protected abstract static class Builder<T extends MetaDataOptions> implements Buildable<T> {
        private Class<? extends Database> dbClass;
        private final Map<String, String> options = new HashMap<>();

        public Builder(Class<? extends Database> dbClass) {
            this.dbClass = dbClass;
        }

        public void addOption(String optionName, String optionValue) {
            options.put(optionName, optionValue);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("");
            for (Map.Entry<String, String> entry : options.entrySet()) {
                sb.append(entry.getKey()).append(" = ").append(entry.getValue());
            }
            return sb.toString();
        }
    }

    public MetaDataOptions(Builder builder) {
        this.dbClass = builder.dbClass;
        optionsMap = builder.options;
    }

    @Override
    public String getOption(String optionName) {
        return optionsMap.get(optionName);
    }

    @Override
    public String getCreateOptionsDefinition() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : optionsMap.entrySet()) {
            sb.append(entry.getKey()).append(" = ").append(entry.getValue()).append('\n');
        }
        return sb.toString();
    }

    @Override
    public List<String> getUpdateOptionsDefinition() {
        List<String> result = new LinkedList<>();
        result.add(getCreateOptionsDefinition());
        return result;
    }

    @Override
    public Map<String, String> getOptionsMap() {
        return Collections.unmodifiableMap(optionsMap);
    }

    @Override
    public Class<? extends Database> getDbClass() {
        return dbClass;
    }

    @Override
    public String toString() {
        return getCreateOptionsDefinition();
    }
}
