package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.DaoSystemException;

import java.util.List;

public interface DataManipulationDao<V,C> {
    //Read N values from container starting with nStart position
    List<V> readN(int nStart, int number, C containerId)  throws DaoSystemException;

    //Delete values all from container
    void deleteAll(C containerId) throws DaoSystemException;

    //Count number of values in container
    int count(C containerId) throws DaoSystemException;
}
