package ua.com.nov.model.entity.data;

import ua.com.nov.model.dao.TableRowMapper;
import ua.com.nov.model.dao.exception.DAOSystemException;

public class ProxyRow<R extends AbstractRow<R>> {
    private R row;
    private TableRowMapper<R> mapper;

    public ProxyRow(TableRowMapper<R> mapper) {
        this.mapper = mapper;
    }

    public ProxyRow(R row) {
        this.row = row;
    }

    public R getRow(AbstractRow.Id<R> id) throws DAOSystemException {
        if (row == null) row = mapper.get(id);
        return row;
    }
}
