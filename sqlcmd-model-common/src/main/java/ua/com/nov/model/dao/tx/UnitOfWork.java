package ua.com.nov.model.dao.tx;

import ua.com.nov.model.dao.exception.MappingSystemException;

public interface UnitOfWork<T> {
    T doInTx() throws MappingSystemException;
}
