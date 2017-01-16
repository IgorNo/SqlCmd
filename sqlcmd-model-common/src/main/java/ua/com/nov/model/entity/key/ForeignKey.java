package ua.com.nov.model.entity.key;

import ua.com.nov.model.entity.column.ColumnID;

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

    private ColumnID columnFK; // foreign key column ID
    private ColumnID columnPK; // primary key column ID that are referenced by the given table's foreign key columns
    private int keySeq;  /* sequence number within foreign key( a value of 1 represents the first column of
                              the foreign key, a value of 2 would represent the second column within the foreign key) */
    private int updateRule; // to rule what happens to a foreign key when the primary key is updated
    private int deleteRule; // to rule what happens to a foreign key when the primary key is deleted

}
