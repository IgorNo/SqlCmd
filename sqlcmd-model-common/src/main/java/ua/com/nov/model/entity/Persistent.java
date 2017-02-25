package ua.com.nov.model.entity;

import ua.com.nov.model.statement.SqlStatementSource;

public interface Persistent<V> extends Unique {

   SqlStatementSource<V> getSqlStmtSource();

   Mappable<V> getRowMapper();
}
