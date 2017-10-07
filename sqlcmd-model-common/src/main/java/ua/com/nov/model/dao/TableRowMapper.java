package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.DAOSystemException;
import ua.com.nov.model.dao.fetch.FetchParameter;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.metadata.table.GenericTable;

import java.util.List;

public interface TableRowMapper<R extends AbstractRow<R>> extends Mapper<AbstractRow.Id<R>, R, GenericTable<R>> {

    GenericTable<R> getTable();

    @Override
    default GenericTable<R> getContainer() {
        return getTable();
    }

    @Override
    R get(AbstractRow.Id<R> id) throws DAOSystemException;

    @Override
    List<R> getAll() throws DAOSystemException;

    @Override
    List<R> getN(int nStart, int number) throws DAOSystemException;

    @Override
    List<R> getFetch(FetchParameter... parameters) throws DAOSystemException;

    @Override
    R add(R row) throws DAOSystemException;

    @Override
    void change(R oldValue, R newValue) throws DAOSystemException;

    @Override
    void delete(R row) throws DAOSystemException;

    @Override
    void deleteAll() throws DAOSystemException;

    @Override
    int size() throws DAOSystemException;
}
