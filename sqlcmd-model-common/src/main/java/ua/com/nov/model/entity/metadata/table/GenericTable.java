package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.data.AbstractRow;

public class GenericTable<R extends AbstractRow<R>> extends Table {

    private Class<R> rowClass;

    public GenericTable(Table table, Class<R> rowClass) {
        super(new Table.Builder(table));
        this.rowClass = rowClass;
        TableMapper.addTable(table.getId(), this);
    }

    public Class<R> getRowClass() {
        return rowClass;
    }

}
