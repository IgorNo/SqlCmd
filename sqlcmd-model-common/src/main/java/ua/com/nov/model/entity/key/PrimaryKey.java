package ua.com.nov.model.entity.key;

import ua.com.nov.model.entity.column.ColumnID;

public class PrimaryKey {
    private ColumnID columnID;
    private int keySeq;  /* sequence number within primary key( a value of 1 represents the first column of
                              the primary key, a value of 2 would represent the second column within the primary key) */
    private String pkName; // primary key name (may be 'null')

    public PrimaryKey(ColumnID columnID, short keySeq, String pkName) {
        this.columnID = columnID;
        this.keySeq = keySeq;
        this.pkName = pkName;
    }

    public ColumnID getColumnID() {
        return columnID;
    }

    public int getKeySeq() {
        return keySeq;
    }

    public String getPkName() {
        return pkName;
    }
}
