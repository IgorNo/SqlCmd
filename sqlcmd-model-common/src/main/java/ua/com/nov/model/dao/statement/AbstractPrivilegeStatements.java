package ua.com.nov.model.dao.statement;

import ua.com.nov.model.entity.metadata.MetaData;
import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.grantee.privelege.Privilege;

public abstract class AbstractPrivilegeStatements implements SqlStatementSource<MetaData<?>, Privilege, Grantee> {

    @Override
    public SqlStatement getCreateStmt(Privilege privilege) {
        return new SqlStatement.Builder("GRANT " + privilege.getCreateStmtDefinition()).build();
    }

    @Override
    public SqlStatement getUpdateStmt(Privilege entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlStatement getDeleteStmt(Privilege privilege) {
        StringBuilder sb = new StringBuilder("REVOKE ").append(privilege.getActions());
        sb.append("\n\tON ").append(privilege.getOnExpression());
        sb.append("\n\tFROM ").append(privilege.getGrantees());
        sb.append(";\n");
        return new SqlStatement.Builder(sb.toString()).build();
    }

    @Override
    public SqlStatement getReadOneStmt(MetaData<?> eId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlStatement getReadAllStmt(Grantee cId) {
        throw new UnsupportedOperationException();
    }
}
