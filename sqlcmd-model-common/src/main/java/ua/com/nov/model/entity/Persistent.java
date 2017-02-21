package ua.com.nov.model.entity;

import ua.com.nov.model.dao.SqlStatementSource;

public interface Persistent<K, V> {
   SqlStatementSource<K, V> getSqlStmtSource();
}
