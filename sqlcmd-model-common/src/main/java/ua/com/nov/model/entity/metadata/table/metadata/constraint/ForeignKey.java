package ua.com.nov.model.entity.metadata.table.metadata.constraint;

import javafx.util.Pair;
import ua.com.nov.model.entity.metadata.table.metadata.column.Column;

import java.util.Map;

public class ForeignKey {
    public enum Rule {
        /**
         * For the field <code>updateRule</code>, indicates that
         * when the primary constraint is updated, the foreign constraint (imported constraint) is changed to agree with it.
         * For the field <code>deleteRule</code>, it indicates that
         * when the primary constraint is deleted, rows that imported that constraint are deleted.
         */
        CASCADE(0, "CASCADE"),

        /**
         * For the field <code>updateRule</code>, indicates that
         * a primary constraint may not be updated if it has been imported by another table as a foreign constraint.
         * For the field <code>deleteRule</code>, indicates that
         * a primary constraint may not be deleted if it has been imported by another table as a foreign constraint.
         */
        RESTRICT(1, "RESTRICT"),

        /**
         * For the field <code>updateRule</code> and <code>deleteRule</code>, indicates that
         * when the primary constraint is updated or deleted, the foreign constraint (imported constraint) is changed to <code>NULL</code>.
         */
       SET_NULL(2, "SET NULL"),

        /**
         * For the field <code>updateRule</code> and <code>deleteRule</code>, indicates that
         * if the primary constraint has been imported, it cannot be updated or deleted.
         */
        NO_ACTION(3, "NO ACTION"),

        /**
         * For the field <code>updateRule</code> and <code>deleteRule</code>, indicates that
         * if the primary constraint is updated or deleted, the foreign constraint (imported constraint) is set to the default value.
         */
       SET_DEFAULT(4, "SET DEFAULT");

        private final String action;
        private final int ordinalNumber;

        Rule(int ordinalNumber, String action) {
            this.action = action;
            this.ordinalNumber = ordinalNumber;
        }

        public String getAction() {
            return action;
        }

        public Rule getRule(int n) {
            for (Rule rule : values()) {
                if (rule.ordinalNumber == n) return rule;
            }
            throw new IllegalArgumentException();
        }
    }

    private String name;
    private Map<Integer, Pair<Column, Column>> foreignKey;

    private Rule updateRule; // to action what happens to a foreign constraint when the primary constraint is updated
    private Rule deleteRule; // to action what happens to a foreign constraint when the primary constraint is deleted

    private ForeignKey() {}

    public static ForeignKey create() {
        return new ForeignKey();
    }

    public int getNumberOfColumns() {
        return foreignKey.size();
    }

    /* keySeq - sequence number within foreign constraint( a value of 1 represents the first column of
                the foreign constraint, a value of 2 would represent the second column within the foreign constraint)
       fkColumm - foreign constraint column
       pkColumn - primary constraint column ID that are referenced by the given table's foreign constraint column
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

    public Rule getUpdateRule() {
        return updateRule;
    }

    public void setUpdateRule(Rule updateRule) {
        this.updateRule = updateRule;
    }

    public Rule getDeleteRule() {
        return deleteRule;
    }

    public void setDeleteRule(Rule deleteRule) {
        this.deleteRule = deleteRule;
    }
}
