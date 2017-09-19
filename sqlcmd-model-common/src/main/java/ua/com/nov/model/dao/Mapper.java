package ua.com.nov.model.dao;

import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.fetch.FetchParameter;
import ua.com.nov.model.entity.Unique;

import java.util.List;

public interface Mapper<I, E extends Unique<I>, C> {

    C getContainer();

    E get(I id) throws MappingSystemException;

    List<E> getAll() throws MappingSystemException;

    List<E> getN(int nStart, int number) throws MappingSystemException;

    List<E> getFetch(FetchParameter... parameters) throws MappingSystemException;

    E add(E entity) throws MappingSystemException;

    void change(E oldValue, E newValue) throws MappingSystemException;

    void delete(E row) throws MappingSystemException;

    void deleteAll() throws MappingSystemException;

    int size() throws MappingSystemException;
}
