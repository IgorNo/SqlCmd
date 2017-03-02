package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.row.RowData;

import java.util.ArrayList;
import java.util.List;

public class TableData<E extends RowData> {
    private final Table table;
    private List<E> rows = new ArrayList<>();   // table data

    public TableData(Table table) {
        this.table = table;
    }
}
