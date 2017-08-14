package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.data.Row;

public class RowTable extends GenericTable<Row> {

    public RowTable(Table table) {
        super(table, Row.class);
    }
}
