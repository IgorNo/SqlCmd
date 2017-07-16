package ua.com.nov.model.dao.service;

import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.impl.AbstractRowDao;
import ua.com.nov.model.dao.impl.RowDao;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.data.Row;
import ua.com.nov.model.entity.metadata.table.Table;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.WeakHashMap;

public class RowMapper {
    private static final Map<AbstractRow.Id, AbstractRow> cachedRow = new WeakHashMap<>();
    private static RowMapper instance = new RowMapper();

    private RowMapper() {
    }

    public static RowMapper getInstance() {
        return instance;
    }

    public <T extends AbstractRow> T get(AbstractRow.Id id, Class<T> clazz, DataSource dataSource) throws DaoSystemException {
        T row = (T) cachedRow.get(id);
        if (row == null) {
            AbstractRowDao<T> dao;
            if (clazz == Row.class) {
                dao = (AbstractRowDao<T>) new RowDao(id.getTable());
            } else {
                dao = new AbstractRowDao<T>() {
                    @Override
                    protected AbstractRowMapper<T, Table> getRowMapper(Table table) {
                        return new AbstractRowMapper<T, Table>(table) {
                            @Override
                            public T mapRow(ResultSet rs, int i) throws SQLException {
                                AbstractRow.Builder<T> row = null;
                                try {
                                    row = (AbstractRow.Builder<T>) Class.forName(clazz.getName() + "$Builder").newInstance();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                }
                                setRowValues(rs, row, table);
                                return row.build();
                            }
                        };
                    }
                };
            }
            dao.setTable(id.getTable()).setDataSource(dataSource);
            row = dao.read(id);
            cachedRow.put(id, row);
        }
        return row;
    }

    public void put(AbstractRow row) {

    }
}
