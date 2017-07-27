package ua.com.nov.model.dao.service;

import org.springframework.jdbc.support.KeyHolder;
import ua.com.nov.model.dao.exception.MappingBusinessLogicException;
import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.fetch.FetchParameter;
import ua.com.nov.model.dao.impl.RowDao;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.metadata.table.Table;

import javax.sql.DataSource;
import java.util.ConcurrentModificationException;
import java.util.List;

public class RDBMapper implements Mapper {
    private DataSource dataSource;
    private boolean checkConcurrentModification;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isCheckConcurrentModification() {
        return checkConcurrentModification;
    }

    public void setCheckConcurrentModification(boolean checkConcurrentModification) {
        this.checkConcurrentModification = checkConcurrentModification;
    }

    @Override
    public <R extends AbstractRow> R get(AbstractRow.Id id) throws MappingSystemException {
        return new RowDao<R>(dataSource).read(id);
    }

    @Override
    public <R extends AbstractRow> List<R> getAll(Table table) throws MappingSystemException {
        return new RowDao<R>(dataSource).readAll(table);
    }

    @Override
    public <R extends AbstractRow> List<R> getN(Table table, int nStart, int number) throws MappingSystemException {
        return new RowDao<R>(dataSource).readN(table, nStart, number);
    }

    @Override
    public <R extends AbstractRow> List<R> getFetch(Table table, FetchParameter... parameters) throws MappingSystemException {
        return new RowDao<R>(dataSource).readFetch(table, parameters);
    }

    @Override
    public <R extends AbstractRow> R add(R row) throws MappingSystemException {
        RowDao<R> dao = new RowDao<>(dataSource);
        KeyHolder keyHolder = dao.insert(row);
        AbstractRow.Builder<R> rowWithId = null;
        if (keyHolder != null) {
            try {
                rowWithId = (AbstractRow.Builder<R>) Class.forName(row.getClass().getName() + "$Builder").newInstance();
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
    public <R extends AbstractRow> void change(R oldValue, R newValue) throws MappingSystemException {
        if (isCheckConcurrentModification()) checkChanges(oldValue);
        new RowDao<R>(dataSource).update(newValue);
    }

    protected <R extends AbstractRow> void checkChanges(R oldValue) throws MappingSystemException {
        R value = get(oldValue.getId());
        if (!oldValue.equals(value)) {
            throw new ConcurrentModificationException(String.format("Row '%s' has been already changed to '%s'.",
                    oldValue, value));
        }
    }

    @Override
    public void delete(AbstractRow row) throws MappingSystemException {
        if (isCheckConcurrentModification()) checkChanges(row);
        new RowDao<>(dataSource).delete(row);
    }

    @Override
    public int size(Table table) throws MappingSystemException {
        return new RowDao<>(dataSource).count(table);
    }
}
