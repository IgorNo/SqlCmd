package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.fetch.FetchParametersSource;

import java.util.List;

public interface DataManipulationDao<E,C> {
    //Read N values from container starting with nStart position
    List<E> readN(int nStart, int number, C containerId)  throws DaoSystemException;

    // Read fetching entities
    <T extends FetchParametersSource<C>> List<E> readFetch(T parameters) throws DaoSystemException;

    //Delete values all from container
    void deleteAll(C containerId) throws DaoSystemException;

    //Count number of values in container
    int count(C containerId) throws DaoSystemException;
}
