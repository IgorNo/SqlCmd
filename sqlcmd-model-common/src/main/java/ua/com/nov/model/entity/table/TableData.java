package ua.com.nov.model.entity.table;

import ua.com.nov.model.entity.row.RowData;

import java.util.ArrayList;
import java.util.List;

public class TableData<E extends RowData> {
    private final TableMetaData tableMetaData;
    private List<E> rows = new ArrayList<>();   // table data

    public TableData(TableMetaData tableMetaData) {
        this.tableMetaData = tableMetaData;
    }
}
