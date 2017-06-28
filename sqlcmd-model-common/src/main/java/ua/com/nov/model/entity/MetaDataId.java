package ua.com.nov.model.entity;

public interface MetaDataId<C> extends Hierarchical<C> {

    String getMdName();

    String getName();

    String getFullName();

}
