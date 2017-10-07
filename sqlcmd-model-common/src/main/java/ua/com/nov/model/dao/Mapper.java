package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.SystemException;
import ua.com.nov.model.dao.fetch.FetchParameter;
import ua.com.nov.model.entity.Unique;

import java.util.List;

public interface Mapper<I, E extends Unique<I>, C> {

    C getContainer();

    E get(I id) throws SystemException;

    List<E> getAll() throws SystemException;

    List<E> getN(int nStart, int number) throws SystemException;

    List<E> getFetch(FetchParameter... parameters) throws SystemException;

    E add(E entity) throws SystemException;

    void change(E oldValue, E newValue) throws SystemException;

    void delete(E row) throws SystemException;

    void deleteAll() throws SystemException;

    int size() throws SystemException;
}
