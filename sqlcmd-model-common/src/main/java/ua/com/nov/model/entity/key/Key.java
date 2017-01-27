package ua.com.nov.model.entity.key;

import ua.com.nov.model.entity.column.Column;

import java.util.Map;
import java.util.TreeMap;

public class Key {
    private Map<Integer, Column> key = new TreeMap<>();

    private Key() {}

    public static Key create() {
        return new Key();
    }

    public int getNumberOfColumns() {
        return key.size();
    }

    /* keySeq - sequence number within primary key( a value of 1 represents the first column of
                the foreign key, a value of 2 would represent the second column within the primary key) */
    public void addColumn(int keySeq, Column column) {
        if (key.put(keySeq, column) != null) throw new IllegalArgumentException();
    }

    public Column getColumn(int keySeq) {
        Column result = key.get(keySeq);
        if (result == null) throw new IllegalArgumentException();
        return result;
    }
}
