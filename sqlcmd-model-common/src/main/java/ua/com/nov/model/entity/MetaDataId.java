package ua.com.nov.model.entity;

public interface MetaDataId<C> extends Persistance<C> {

    String getMdName();

    String getName();

    String getFullName();

}
