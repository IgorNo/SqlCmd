package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.data.Row;
import ua.com.nov.model.entity.metadata.table.Table;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowDao extends AbstractRowDao<Row> {
    @Override
    protected AbstractRowMapper<Row, Table> getRowMapper(Table table) {
        return new AbstractRowMapper<Row, Table>(table) {
            @Override
            public Row mapRow(ResultSet rs, int i) throws SQLException {
                Row.Builder row = new Row.Builder(table);
                setRowValues(rs, row, table);
                return row.build();
            }
        };
    }
}
