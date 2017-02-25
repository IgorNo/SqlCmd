package ua.com.nov.model.entity;

import ua.com.nov.model.dao.SqlStatementSource;

public interface Persistent<V> extends Unique {

   SqlStatementSource<V> getSqlStmtSource();

   Mappable<V> getRowMapper();
}
