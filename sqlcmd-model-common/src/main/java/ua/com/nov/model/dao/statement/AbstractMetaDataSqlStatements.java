package ua.com.nov.model.dao.statement;

import ua.com.nov.model.entity.*;

public abstract class AbstractMetaDataSqlStatements<K extends Persistent, V extends Unique<K> & Optionable, C>
        extends BaseSqlStmtSource<K, V, C> {

    public SqlStatement getCreateStmt(V value) {
        MetaDataOptions mdOptions = value.getMdOptions();
        if (mdOptions != null) {
            if (!(mdOptions instanceof MdCreateOptions))
                throw new IllegalArgumentException("MetaDataOptions must be MdCreateOptions.");
        }
        return new SqlStatement.Builder(String.format("CREATE %s", value.toString())).build();
    }

    public SqlStatement getUpdateStmt(V value) {
        MetaDataOptions mdOptions = value.getMdOptions();
        if (!(mdOptions instanceof MdUpdateOptions))
            throw new IllegalArgumentException("MetaDataOptions must be MdUpdateOptions.");

        StringBuilder sql = new StringBuilder();
        for (String s : mdOptions.getOptionList()) {
            sql.append(String.format("ALTER %s %s %s ;\n", value.getId().getMdName(), value.getId().getFullName(), s));
        }
        return new SqlStatement.Builder(sql.toString()).build();
    }

    public SqlStatement getDeleteStmt(K key) {
        return new SqlStatement.Builder(String.format("DROP %s %s", key.getMdName(), key.getFullName())).build();
    }

}
