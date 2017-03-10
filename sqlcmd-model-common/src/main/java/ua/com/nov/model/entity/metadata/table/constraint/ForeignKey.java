package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.Column;
import ua.com.nov.model.entity.metadata.table.TableId;

import java.util.HashMap;
import java.util.Map;

public class ForeignKey extends Key {
    private final Map<Integer, Column> pkKey;
    private final Rule updateRule; // to action what happens to a foreign constraint when the primary constraint is updated
    private final Rule deleteRule; // to action what happens to a foreign constraint when the primary constraint is deleted

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


    public final static class Builder extends Key.Builder {
        private Map<Integer, Column> pkKey  = new HashMap<>();
        private Rule updateRule = Rule.NO_ACTION; /* to action what happens to a foreign constraint
                                                           when the primary constraint is updated */
        private Rule deleteRule = Rule.NO_ACTION; /* to action what happens to a foreign constraint
                                                           when the primary constraint is deleted */

        public Builder(TableId tableId, String keyName, String fkColumm, Column pkColumn) {
            super(tableId, keyName, fkColumm);
            addColumn(1, fkColumm, pkColumn);
        }

        public Builder(String fkColumm, Column pkColumn) {
            this(null, null, fkColumm, pkColumn);
        }

        /* keySeq - sequence number within foreign constraint( a value of 1 represents the first column of
                the foreign constraint, a value of 2 would represent the second column within the foreign constraint)
            fkColumm - foreign constraint column
            pkColumn - primary constraint column ID that are referenced by the given table's foreign constraint column
       */
        public Builder addColumn(int keySeq, String fkColumm, Column pkColumn) {
            super.addColumn(keySeq, fkColumm);
            if ( pkKey.put(keySeq, pkColumn) != null) {
                throw new IllegalArgumentException(String.format("Column '%s' already exists in  in this foreign key.",
                        pkColumn));
            }
            return this;
        }

        public Builder updateRule(Rule updateRule) {
            this.updateRule = updateRule;
            return this;
        }

        public Builder deleteRule(Rule deleteRule) {
            this.deleteRule = deleteRule;
            return this;
        }

        public ForeignKey build() {
            return new ForeignKey(this);
        }
    }

    protected ForeignKey(Builder builder) {
        super(builder);
        this.pkKey = builder.pkKey;
        this.updateRule = builder.updateRule;
        this.deleteRule = builder.deleteRule;
    }

    public int getNumberOfColumns() {
        return pkKey.size();
    }

    public String getFkColumn(int keySeq) {
        return super.getColumnName(keySeq);
    }

    public Column getPkColumn(int keySeq) {
        Column result = pkKey.get(keySeq);
        if (result == null) throw new IllegalArgumentException();
        return result;
    }

    public Rule getUpdateRule() {
        return updateRule;
    }

    public Rule getDeleteRule() {
        return deleteRule;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(String.format(super.toString(), "FOREIGN KEY"));
        sb.append(" REFERENCES ").append(pkKey.get(1).getId().getFullName());
        String s = "  (";
        for (Column col : pkKey.values()) {
            sb.append(s).append(col.getName());
            if (s.isEmpty()) s = ",";
        }

        sb.append(") ON DELETE ").append(deleteRule).append(" ON UPDATE ").append(updateRule);

        return sb.toString();
    }

}

