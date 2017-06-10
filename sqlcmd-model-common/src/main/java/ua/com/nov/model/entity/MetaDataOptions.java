package ua.com.nov.model.entity;

import ua.com.nov.model.entity.metadata.server.Server;

import java.util.*;

public abstract class MetaDataOptions<E> implements Optional<E> {
    private final Class<? extends Server> serverClass;
    private final Map<String, String> options;

    public abstract static class Builder<T extends MetaDataOptions> implements Buildable<T> {
        protected Class<? extends Server> serverClass;
        protected Map<String, String> options = new TreeMap<>();

        public Builder(Class<? extends Server> serverClass) {
            this.serverClass = serverClass;
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

    protected MetaDataOptions(Builder builder) {
        this.serverClass = builder.serverClass;
        this.options = Collections.unmodifiableMap(builder.options);
    }

    @Override
    public String getOption(String optionName) {
        return options.get(optionName);
    }

//    @Override
//    public String getCreateOptionsDefinition() {
//        StringBuilder sb = new StringBuilder();
//        String s = "";
//        for (Map.Entry<String, String> entry : options.entrySet()) {
//            sb.append(s).append(entry.getKey()).append(" = ").append(entry.getValue());
//            s = "\n";
//        }
//        return sb.toString();
//    }

    @Override
    public List<String> getUpdateOptionsDefinition() {
        List<String> result = new LinkedList<>();
        result.add(getCreateOptionsDefinition());
        return result;
    }

    @Override
    public Map<String, String> getOptionsMap() {
        return options;
    }

    @Override
    public Class<? extends Server> getServerClass() {
        return serverClass;
    }

    @Override
    public String toString() {
        return getCreateOptionsDefinition();
    }
}
