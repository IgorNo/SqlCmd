package ua.com.nov.model.statement;

import ua.com.nov.model.entity.metadata.AbstractMetaData;
import ua.com.nov.model.entity.metadata.AbstractMetaDataId;

public abstract class AbstractMetaDataSqlStatements {

    public SqlStatement getCreateStmt(AbstractMetaData<AbstractMetaDataId> value) {
        return new SqlStatement.Builder("CREATE %s", value.getId().getMdName(), value.toString()).build();
    }

    public SqlStatement getDeleteStmt(AbstractMetaDataId key) {
        return new SqlStatement.Builder("DROP %s %s", key.getMdName(), key.getFullName()).build();
    }

    public SqlStatement getUpdateStmt(AbstractMetaData<AbstractMetaDataId> value) {
        return new SqlStatement.Builder("ALTER %s %s %s", value.getId().getMdName(),
                value.getId().getFullName(), value.getAlterExpression()).build();
    }

}
