package ua.com.nov.model.dao.statement;

import ua.com.nov.model.entity.MetaData;
import ua.com.nov.model.entity.MetaDataId;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.AbstractMetaData;

public abstract class AbstractDatabaseMdSqlStatements
        <I extends AbstractMetaData.Id<C>, E extends Unique<I> & MetaData, C extends MetaDataId>
        implements DataDefinitionSqlStmtSource<I, E, C> {

    @Override
    public SqlStatement getCreateStmt(E entity) {
        String comment = getCommentStmt(entity);
        String sql = "CREATE " + entity.getCreateStmtDefinition(null);
        if (!comment.isEmpty()) sql += ";" + comment;
        return new SqlStatement.Builder(sql).build();
    }

    protected String getCommentStmt(E entity) {
        if (entity.getViewName() == null) return "";
        return String.format("\nCOMMENT ON %s %s IS '%s'",
                entity.getId().getMdName(), entity.getId().getFullName(), entity.getViewName());
    }

    @Override
    public SqlStatement getCreateIfNotExistsStmt(E entity) {
        String comment = getCommentStmt(entity);
        String sql = "CREATE " + entity.getCreateStmtDefinition("IF NOT EXISTS");
        if (!comment.isEmpty()) sql += ";" + comment;
        return new SqlStatement.Builder(sql).build();
    }

    @Override
    public SqlStatement getUpdateStmt(E entity) {
        StringBuilder sql = new StringBuilder();
        String s = "";
        if (entity.getOptions() != null) {
            for (String option : entity.getOptions().getUpdateOptionsDefinition()) {
                sql.append(s);
                sql.append(String.format("ALTER %s %s %s", entity.getId().getMdName(), entity.getId().getFullName(), option));
                s = ";\n";
            }
            sql.append(';');
        }
        sql.append(getCommentStmt(entity));
        return new SqlStatement.Builder(sql.toString()).build();
    }

    @Override
    public SqlStatement getDeleteStmt(E entity) {
        return new SqlStatement.Builder(String.format("DROP %s %s",
                entity.getId().getMdName(), entity.getId().getFullName())).build();
    }

    @Override
    public SqlStatement getDeleteIfExistStmt(E entity) {
        return new SqlStatement.Builder(String.format("DROP %s IF EXISTS %s",
                entity.getId().getMdName(), entity.getId().getFullName())).build();
    }

    @Override
    public SqlStatement getRenameStmt(E entity, String newName) {
        return new SqlStatement.Builder(String.format("ALTER %s %s RENAME TO %s",
                entity.getId().getMdName(), entity.getId().getFullName(), newName)).build();
    }

}
