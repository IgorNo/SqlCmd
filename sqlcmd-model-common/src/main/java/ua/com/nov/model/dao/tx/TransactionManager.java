package ua.com.nov.model.dao.tx;

import ua.com.nov.model.dao.exception.MappingSystemException;

public interface TransactionManager {

    <T> T doInTransaction(UnitOfWork<T> unitOfWork) throws MappingSystemException;
}
