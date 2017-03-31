package ua.com.nov.model.entity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class MetaDataOptions {
    private final String prefixOptins;
    private final String existOptions;
    protected final List<String> optionList = new LinkedList<>();

    protected abstract static class Builder<V> implements Buildable<V> {
        private String prefixOptins = "";
        private String existOptions = "";

        protected void setPrefixOptins(String prefixOptins) {
            this.prefixOptins = prefixOptins;
        }

        protected void setExistOptions(String existOptions) {
            this.existOptions = existOptions;
        }
    }

    protected MetaDataOptions(Builder builder) {
        this(builder.prefixOptins, builder.existOptions);
    }

    public MetaDataOptions() {
        this("", "");
    }

    public MetaDataOptions(String prefixOptins, String existOptions) {
        this.prefixOptins = prefixOptins;
        this.existOptions = existOptions;
    }

    public String getPrefixOptins() {
        return prefixOptins;
    }

    public String getExistOptions() {
        return existOptions;
    }

    public List<String> getOptionList() {
        return Collections.unmodifiableList(optionList);
    }
}
