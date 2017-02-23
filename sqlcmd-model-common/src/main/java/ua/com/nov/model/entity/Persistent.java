package ua.com.nov.model.entity;

import ua.com.nov.model.dao.SqlStatementSource;

public interface Persistent<K, V, C> {
   K getId();

   C getContainer();

   SqlStatementSource<V, C> getSqlStmtSource();

   Mappable<V> getRowMapper();
}
