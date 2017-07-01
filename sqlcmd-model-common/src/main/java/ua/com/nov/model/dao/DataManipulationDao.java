package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.DaoSystemException;

import java.util.List;

public interface DataManipulationDao<E> {

    // Insert value into table
    long insert(E entity) throws DaoSystemException;

    //Read N values from container starting with nStart position
    List<E> readN(long nStart, int number) throws DaoSystemException;

    List<E> readAll() throws DaoSystemException;

//    // Read fetching entities
//    <T extends FetchParametersSource<C>> List<E> readFetch(T parameters) throws DaoSystemException;

    //Delete values all from container
    void deleteAll() throws DaoSystemException;

    //Count number of values in container
    int count() throws DaoSystemException;
}
