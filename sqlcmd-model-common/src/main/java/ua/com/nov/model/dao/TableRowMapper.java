package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.MappingSystemException;
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
    R get(AbstractRow.Id<R> id) throws MappingSystemException;

    @Override
    List<R> getAll() throws MappingSystemException;

    @Override
    List<R> getN(int nStart, int number) throws MappingSystemException;

    @Override
    List<R> getFetch(FetchParameter... parameters) throws MappingSystemException;

    @Override
    R add(R row) throws MappingSystemException;

    @Override
    void change(R oldValue, R newValue) throws MappingSystemException;

    @Override
    void delete(R row) throws MappingSystemException;

}
