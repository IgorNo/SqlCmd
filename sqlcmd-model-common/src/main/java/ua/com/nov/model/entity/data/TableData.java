package ua.com.nov.model.entity.data;

import ua.com.nov.model.entity.metadata.table.Table;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class TableData<R extends AbstractRow> {

    private Table table;
    private Map<AbstractRow.Id, R> rows = new ConcurrentHashMap<>();   // table data

    public TableData() {
    }

    protected TableData table(Table table) {
        this.table = table;
        return this;
    }
}
