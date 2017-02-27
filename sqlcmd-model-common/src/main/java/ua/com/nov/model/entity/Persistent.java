package ua.com.nov.model.entity;

import ua.com.nov.model.statement.SqlStatementSource;

public interface Persistent<V> {

   <K> SqlStatementSource<K,V> getSqlStmtSource();

   Mappable<V> getRowMapper();
}
