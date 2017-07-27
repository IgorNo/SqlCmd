package ua.com.nov.model.dao.statement;

import ua.com.nov.model.dao.fetch.FetchParameter;

public interface DataManipulationSqlStmtSource<I,E,C> extends SqlStatementSource<I,E,C> {

    SqlStatement getDeleteAllStmt(C cId);

    SqlStatement getCountStmt(C cId);

    SqlStatement getReadNStmt(C cId, long nStart, int number);

    SqlStatement getReadFetchStmt(C cId, FetchParameter... parameters);
}
