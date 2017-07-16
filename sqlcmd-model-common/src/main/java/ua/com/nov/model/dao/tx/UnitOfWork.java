package ua.com.nov.model.dao.tx;

import ua.com.nov.model.dao.exception.DaoSystemException;

public interface UnitOfWork<T> {
    T doInTx() throws DaoSystemException;
}
