package ua.com.nov.model.entity;

import ua.com.nov.model.entity.metadata.server.Server;

public interface Hierarchical<C> {

   Server getServer();

   String getMdName();

   String getName();

   String getFullName();

   C getContainerId();

}
