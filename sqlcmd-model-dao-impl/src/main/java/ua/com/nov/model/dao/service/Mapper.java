package ua.com.nov.model.dao.service;

import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.fetch.FetchParameter;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.metadata.table.Table;

import java.util.List;

public interface Mapper {

    <R extends AbstractRow> R get(AbstractRow.Id id) throws MappingSystemException;

    <R extends AbstractRow> List<R> getAll(Table table) throws MappingSystemException;

    <R extends AbstractRow> List<R> getN(Table table, int nStart, int number) throws MappingSystemException;

    <R extends AbstractRow> List<R> getFetch(Table table, FetchParameter... parameters) throws MappingSystemException;

    <R extends AbstractRow> R add(R row) throws MappingSystemException;

    <R extends AbstractRow> void change(R oldValue, R newValue) throws MappingSystemException;

    void delete(AbstractRow row) throws MappingSystemException;

    int size(Table table) throws MappingSystemException;
}
