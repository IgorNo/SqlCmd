package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.SqlExecutor;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.entity.metadata.MetaData;
import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.grantee.privelege.Privilege;

import javax.sql.DataSource;
import java.util.List;

public class PrivilegeDao {

    private SqlExecutor<Privilege> executor;

    public PrivilegeDao() {
    }

    public PrivilegeDao(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public DataSource getDataSource() {
        return executor.getDataSource();
    }

    public PrivilegeDao setDataSource(DataSource dataSource) {
        this.executor = new DDLSqlExecutor<>(dataSource);
        return this;
    }

    public void grant(Privilege privilege) throws DaoSystemException {
        executor.executeUpdateStmt(privilege.getGranteeList().get(0).getServer().getPrivelegeStmtSource()
                .getCreateStmt(privilege));
    }

    public void revoke(Privilege privilege) throws DaoSystemException {
        executor.executeUpdateStmt(privilege.getGranteeList().get(0).getServer().getPrivelegeStmtSource()
                .getCreateStmt(privilege));
    }

    public Privilege read(MetaData metaData) throws DaoSystemException {
        throw new UnsupportedOperationException();
    }

    public List<Privilege> readAll(Grantee grantee) throws DaoSystemException {
        throw new UnsupportedOperationException();
    }
}
