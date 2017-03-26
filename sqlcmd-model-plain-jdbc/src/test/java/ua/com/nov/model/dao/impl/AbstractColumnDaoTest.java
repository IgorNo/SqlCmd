package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.Dao;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMdId;
import ua.com.nov.model.entity.metadata.table.column.Column;

import java.sql.SQLException;

public abstract class AbstractColumnDaoTest {
    public static final Dao<TableMdId, Column, TableId> DAO = new ColumnDao();

    private Column testColum1;

    protected abstract AbstractTableDaoTest getTableDaoTest();

    protected void createTestData() throws SQLException {
        if (DAO.getDataSource() == null) {
            DAO.setDataSource(getTableDaoTest().getDataSource());
            testColum1 = new Column.Builder(getTableDaoTest().getCustomers().getId(), "test_column", null)
                    .build();
        }
    }

}