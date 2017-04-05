package ua.com.nov.model.entity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class MetaDataOptions {
    private final String existOptions;
    protected final List<String> optionList = new LinkedList<>();

    protected abstract static class Builder<V> implements Buildable<V> {
        private String existOptions;

        public Builder() {
        }

        protected void setExistOptions(String existOptions) {
            this.existOptions = existOptions;
        }
    }

    protected MetaDataOptions(Builder builder) {
        this.existOptions = builder.existOptions;
    }

    public String getExistOptions() {
        return existOptions;
    }

    public List<String> getOptionList() {
        return Collections.unmodifiableList(optionList);
    }
}
