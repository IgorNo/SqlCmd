package ua.com.nov.model.entity.metadata.grantee;

import ua.com.nov.model.entity.MetaDataOptions;

public abstract class GranteeOptions<E extends Grantee> extends MetaDataOptions<E> {
    public GranteeOptions(Builder builder) {
        super(builder);
    }
}
