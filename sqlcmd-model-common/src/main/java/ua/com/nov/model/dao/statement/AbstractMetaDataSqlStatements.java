package ua.com.nov.model.dao.statement;

import ua.com.nov.model.entity.Hierarchical;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.MetaDataId;

public abstract class AbstractMetaDataSqlStatements
        <I extends MetaDataId<C>, E extends Unique<I> & Persistent, C extends Hierarchical>
        implements DataDefinitionSqlStmtSource<I, E, C> {

    @Override
    public SqlStatement getCreateStmt(E entity) {
        return new SqlStatement.Builder("CREATE " + entity.getCreateStmtDefinition(null) + ";" +
                getCommentStmt(entity)).build();
    }

    protected String getCommentStmt(E entity) {
        if (entity.getViewName() == null) return "";
        return String.format("COMMENT ON %s %s IS '%s'",
                entity.getId().getMdName(), entity.getId().getFullName(), entity.getViewName());
    }

    @Override
    public SqlStatement getCreateIfNotExistsStmt(E entity) {
        return new SqlStatement.Builder("CREATE " + entity.getCreateStmtDefinition("IF NOT EXISTS") +
                ";" + getCommentStmt(entity)).build();
    }

    @Override
    public SqlStatement getUpdateStmt(E entity) {
        StringBuilder sql = new StringBuilder();
        if (!getCommentStmt(entity).isEmpty()) sql.append('\n').append(getCommentStmt(entity)).append(';');
        for (String s : entity.getOptions().getUpdateOptionsDefinition()) {
            sql.append(String.format("\nALTER %s %s %s ;", entity.getId().getMdName(), entity.getId().getFullName(), s));
        }
        return new SqlStatement.Builder(sql.toString()).build();
    }

    @Override
    public SqlStatement getDeleteStmt(I eId) {
        return new SqlStatement.Builder(String.format("DROP %s %s", eId.getMdName(), eId.getFullName())).build();
    }

    @Override
    public SqlStatement getDeleteIfExistStmt(I eId) {
        return new SqlStatement.Builder(String.format("DROP %s IF EXISTS %s", eId.getMdName(), eId.getFullName())).build();
    }

    @Override
    public SqlStatement getRenameStmt(I eId, String newName) {
        return new SqlStatement.Builder(String.format("ALTER %s %s RENAME TO %s",
                eId.getMdName(), eId.getFullName(), newName)).build();
    }
}
