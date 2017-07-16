package ua.com.nov.model.dao.tx;

import ua.com.nov.model.dao.exception.DaoSystemException;

public interface TransactionManager {

    <T> T doInTransaction(UnitOfWork<T> unitOfWork) throws DaoSystemException;
}
