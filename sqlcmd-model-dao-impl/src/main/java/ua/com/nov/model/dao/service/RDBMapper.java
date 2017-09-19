package ua.com.nov.model.dao.service;

import org.springframework.jdbc.support.KeyHolder;
import ua.com.nov.model.dao.TableRowMapper;
import ua.com.nov.model.dao.exception.MappingBusinessLogicException;
import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.fetch.FetchParameter;
import ua.com.nov.model.dao.impl.RowDao;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.data.Row;
import ua.com.nov.model.entity.metadata.table.GenericTable;
import ua.com.nov.model.entity.metadata.table.TableMapper;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;

import javax.sql.DataSource;
import java.util.ConcurrentModificationException;
import java.util.List;

public class RDBMapper<R extends AbstractRow<R>> implements TableRowMapper<R> {
    private DataSource dataSource;
    private GenericTable<R> table;
    private boolean checkConcurrentModification;
    private boolean immediateInitialization;

    public RDBMapper() {
    }

    public RDBMapper(GenericTable<R> table) {
        this.table = table;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isCheckConcurrentModification() {
        return checkConcurrentModification;
    }

    public void setCheckConcurrentModification(boolean checkConcurrentModification) {
        this.checkConcurrentModification = checkConcurrentModification;
    }

    public boolean isImmediateInitialization() {
        return immediateInitialization;
    }

    public void setImmediateInitialization(boolean immediateInitialization) {
        this.immediateInitialization = immediateInitialization;
    }

    @Override
    public GenericTable<R> getTable() {
        return table;
    }

    public void setTable(GenericTable<R> table) {
        this.table = table;
    }

    @Override
    public R get(AbstractRow.Id<R> id) throws MappingSystemException {
        R row = new RowDao<R>(dataSource).read(id);
        initForeignKey(row);
        return row;
    }

    private void initForeignKey(R row) throws MappingSystemException {
        List<ForeignKey> foreignKeys = row.getTable().getForeignKeyList();
        if (foreignKeys != null) {
            for (ForeignKey foreignKey : foreignKeys) {
                GenericTable<?> table = TableMapper.getGenericTable(foreignKey.getTableId());
                row.setForeignKey(new RDBMapper<>(TableMapper.getGenericTable(table.getId(), table.getRowClass())));
                if (isImmediateInitialization()) row.getForeignKeyValue(table);
            }
        }
    }

    @Override
    public List<R> getAll() throws MappingSystemException {
        List<R> result = new RowDao<R>(dataSource).readAll(table);
        for (R row : result) {
            initForeignKey(row);
        }
        return result;
    }

    @Override
    public List<R> getN(int nStart, int number) throws MappingSystemException {
        List<R> result = new RowDao<R>(dataSource).readN(table, nStart, number);
        for (R row : result) {
            initForeignKey(row);
        }
        return result;
    }

    @Override
    public List<R> getFetch(FetchParameter... parameters) throws MappingSystemException {
        List<R> result = new RowDao<R>(dataSource).readFetch(table, parameters);
        for (R row : result) {
            initForeignKey(row);
        }
        return result;
    }

    @Override
    public R add(R row) throws MappingSystemException {
        RowDao<R> dao = new RowDao<>(dataSource);
        KeyHolder keyHolder = dao.insert(row);
        AbstractRow.Builder<R> rowWithId = null;
        if (keyHolder != null) {
            try {
                if (row.getClass() == Row.class) {
                    rowWithId = (AbstractRow.Builder<R>) new Row.Builder((GenericTable<Row>) table);
                } else {
                    rowWithId = (AbstractRow.Builder<R>) Class.forName(row.getClass().getName() + "$Builder").newInstance();
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new MappingBusinessLogicException("Create Builder Error.\n", e);
            }
            rowWithId.assign(row);
            rowWithId.setId(keyHolder);
            row = rowWithId.build();
        }
        return row;
    }

    @Override
    public void change(R oldValue, R newValue) throws MappingSystemException {
        if (isCheckConcurrentModification()) checkChanges(oldValue);
        new RowDao<R>(dataSource).update(newValue);
    }

    private void checkChanges(R oldValue) throws MappingSystemException {
        R value = get(oldValue.getId());
        if (!oldValue.equals(value)) {
            throw new ConcurrentModificationException(String.format("Row '%s' has been already changed to '%s'.",
                    oldValue, value));
        }
    }

    @Override
    public void delete(R row) throws MappingSystemException {
        if (isCheckConcurrentModification()) checkChanges(row);
        new RowDao<R>(dataSource).delete(row);
    }

    @Override
    public void deleteAll() throws MappingSystemException {
        new RowDao<R>(dataSource).deleteAll(table);
    }

    @Override
    public int size() throws MappingSystemException {
        return new RowDao<>(dataSource).count(table);
    }
}
