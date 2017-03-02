package ua.com.nov.model.entity;

import ua.com.nov.model.entity.metadata.database.Database;

public interface Persistent{
   Database getDb();
}
