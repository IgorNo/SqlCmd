package ua.com.nov.model.entity;

import ua.com.nov.model.entity.metadata.database.Database;

public interface Persistent<C> {

   Database getDb();

   String getMdName();

   String getName();

   String getFullName();

   C getContainerId();

}
