package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.SqlExecutor;
import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.entity.metadata.AbstractMetaData;
import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.grantee.privelege.OnDeleteOptions;
import ua.com.nov.model.entity.metadata.grantee.privelege.Privilege;

import javax.sql.DataSource;
import java.util.List;

public class PrivilegeDao {

    private SqlExecutor executor;

    public PrivilegeDao() {
    }

    public PrivilegeDao(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public DataSource getDataSource() {
        return executor.getDataSource();
    }

    public PrivilegeDao setDataSource(DataSource dataSource) {
        this.executor = new DDLSqlExecutor(dataSource);
        return this;
    }

    public void grant(Privilege privilege) throws MappingSystemException {
        executor.executeUpdateStmt(privilege.getGranteeList().get(0).getServer().getPrivelegeStmtSource()
                .getCreateStmt(privilege));
    }

    public void revoke(Privilege privilege, OnDeleteOptions option) throws MappingSystemException {
        SqlStatement sqlStatement = privilege.getGranteeList().get(0).getServer().getPrivelegeStmtSource()
                .getDeleteStmt(privilege);
        if (option != null)
            sqlStatement = new SqlStatement.Builder(sqlStatement.getSql() + " " + option).build();
        executor.executeUpdateStmt(sqlStatement);
    }

    public Privilege read(AbstractMetaData metaData) throws MappingSystemException {
        throw new UnsupportedOperationException();
    }

    public List<Privilege> readAll(Grantee grantee) throws MappingSystemException {
        throw new UnsupportedOperationException();
    }
}
