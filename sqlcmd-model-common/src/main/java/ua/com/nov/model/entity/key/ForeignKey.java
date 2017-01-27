package ua.com.nov.model.entity.key;

import javafx.util.Pair;
import ua.com.nov.model.entity.column.Column;

import java.util.Map;

public class ForeignKey {
    /**
     * For the field <code>updateRule</code>, indicates that
     * when the primary key is updated, the foreign key (imported key) is changed to agree with it.
     * For the field <code>deleteRule</code>, it indicates that
     * when the primary key is deleted, rows that imported that key are deleted.
     */
    public static final int RULE_CASCADE = 0;

    /**
     * For the field <code>updateRule</code>, indicates that
     * a primary key may not be updated if it has been imported by another table as a foreign key.
     * For the field <code>deleteRule</code>, indicates that
     * a primary key may not be deleted if it has been imported by another table as a foreign key.
     */
    public static final int RULE_RESTRICT = 1;

    /**
     * For the field <code>updateRule</code> and <code>deleteRule</code>, indicates that
     * when the primary key is updated or deleted, the foreign key (imported key) is changed to <code>NULL</code>.
     */
    public static final int RULE_SET_NULL = 2;

    /**
     * For the field <code>updateRule</code> and <code>deleteRule</code>, indicates that
     * if the primary key has been imported, it cannot be updated or deleted.
     */
    public static final int RULE_NO_ACTION = 3;

    /**
     * For the field <code>updateRule</code> and <code>deleteRule</code>, indicates that
     * if the primary key is updated or deleted, the foreign key (imported key) is set to the default value.
     */
    public static final int RULE_SET_DEFAULT = 4;

    /**
     * Indicates deferrability.  See SQL-92 for a definition.
     */
    public static final int RULE_INITIALLY_DEFFERED = 5;

    /**
     * Indicates deferrability.  See SQL-92 for a definition.
     */
    public static final int RULE_INITIALLY_IMMEDIATE = 6;

    /**
     * Indicates deferrability.  See SQL-92 for a definition.
     */
    public static final int RULE_NOT_DEFERRABLE = 7;

    private Map<Integer, Pair<Column, Column>> foreignKey;

    private int updateRule = 4; // to rule what happens to a foreign key when the primary key is updated
    private int deleteRule = 4; // to rule what happens to a foreign key when the primary key is deleted

    private ForeignKey() {}

    public static ForeignKey create() {
        return new ForeignKey();
    }

    public int getNumberOfColumns() {
        return foreignKey.size();
    }

    /* keySeq - sequence number within foreign key( a value of 1 represents the first column of
                the foreign key, a value of 2 would represent the second column within the foreign key)
       fkColumm - foreign key column
       pkColumn - primary key column ID that are referenced by the given table's foreign key columns
       */
    public void addColumns(int keySeq, Column fkColumm, Column pkColumn) {
        if (foreignKey.put(keySeq, new Pair<Column, Column>(fkColumm, pkColumn)) != null) throw new IllegalArgumentException();
    }

    public Column getFkColumn(int keySeq) {
        Column result = foreignKey.get(keySeq).getKey();
        if (result == null) throw new IllegalArgumentException();
        return result;
    }

    public Column getPkColumn(int keySeq) {
        Column result = foreignKey.get(keySeq).getValue();
        if (result == null) throw new IllegalArgumentException();
        return result;
    }

    public int getUpdateRule() {
        return updateRule;
    }

    public void setUpdateRule(int updateRule) {
        this.updateRule = updateRule;
    }

    public int getDeleteRule() {
        return deleteRule;
    }

    public void setDeleteRule(int deleteRule) {
        this.deleteRule = deleteRule;
    }
}
