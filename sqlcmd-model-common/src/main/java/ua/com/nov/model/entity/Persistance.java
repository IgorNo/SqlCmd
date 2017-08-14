package ua.com.nov.model.entity;

import ua.com.nov.model.entity.metadata.server.Server;

public interface Persistance<C> extends Id<C> {

    Server getServer();
}
