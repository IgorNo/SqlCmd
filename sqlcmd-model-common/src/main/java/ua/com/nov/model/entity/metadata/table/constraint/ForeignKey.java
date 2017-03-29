package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMdId;

import java.util.Map;
import java.util.TreeMap;

public class ForeignKey extends Key {
    private final Map<Integer, TableMdId> pkKey;
    private final Rule updateRule; // to action what happens to a foreign constraint when the primary constraint is updated
    private final Rule deleteRule; // to action what happens to a foreign constraint when the primary constraint is deleted
    private final Match match;

    public enum Match {
        FULL, PARTIAL, SIMPLE
    }

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

        public static Rule getRule(int n) {
            for (Rule rule : values()) {
                if (rule.ordinalNumber == n) return rule;
            }
            throw new IllegalArgumentException();
        }

        @Override
        public String toString() {
            return action;
        }
    }

    public final static class Builder extends Key.Builder {
        private Map<Integer, TableMdId> pkKey  = new TreeMap<>();
        private Rule updateRule; /* to action what happens to a foreign constraint
                                                           when the primary constraint is updated */
        private Rule deleteRule; /* to action what happens to a foreign constraint
                                                           when the primary constraint is deleted */
        private Match match;

        public Builder(String keyName, Table.Id tableId) {
            super(keyName, tableId);
        }

        public Builder(String keyName, Table.Id tableId, String fkColumm, TableMdId pkColumn) {
            super(keyName, tableId, fkColumm);
            addPkColumn(1, pkColumn);
        }

        public Builder(String fkColumm, TableMdId pkColumn) {
            this(null, null, fkColumm, pkColumn);
        }

        /* keySeq - sequence number within foreign constraint( a value of 1 represents the first column of
                the foreign constraint, a value of 2 would represent the second column within the foreign constraint)
            fkColumm - foreign constraint column
            pkColumn - primary constraint column ID that are referenced by the given table's foreign constraint column
       */
        public Builder addColumnPair(int keySeq, String fkColumm, TableMdId pkColumn) {
            super.addColumn(keySeq, fkColumm);
            addPkColumn(keySeq, pkColumn);
            return this;
        }

        public Builder addColumnPair(String fkColumm, TableMdId pkColumn) {
            super.addColumn(fkColumm);
            addPkColumn(getKeySeq(), pkColumn);
            return this;
        }

        private Builder addPkColumn(int keySeq, TableMdId pkColumn) {
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

        public Builder updateRule(int updateRule) {
            this.updateRule = Rule.getRule(updateRule);
            return this;
        }

        public Builder deleteRule(Rule deleteRule) {
            this.deleteRule = deleteRule;
            return this;
        }

        public Builder deleteRule(int deleteRule) {
            this.deleteRule = Rule.getRule(deleteRule);
            return this;
        }

        public Builder match(Match match) {
            this.match = match;
            return this;
        }

        public ForeignKey build() {
            return new ForeignKey(this);
        }
    }

    // вложенный класс создатся для обеспечения уникальности ключей
    public static class Id extends TableMdId {
        public Id(Table.Id containerId, String name) {
            super(containerId, name);
        }

        @Override
        public String getMdName() {
            return "FOREIGN KEY";
        }
    }

    public ForeignKey(Builder builder) {
        super(builder, new Id(builder.getTableId(), builder.getName()));
        this.pkKey = builder.pkKey;
        this.updateRule = builder.updateRule;
        this.deleteRule = builder.deleteRule;
        this.match = builder.match;
    }

    public int getNumberOfColumns() {
        return pkKey.size();
    }

    public String getFkColumn(int keySeq) {
        return super.getColumn(keySeq).getName();
    }

    public TableMdId getPkColumn(int keySeq) {
        TableMdId result = pkKey.get(keySeq);
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
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" REFERENCES ").append(pkKey.get(1).getTableId().getFullName());

        String s = " (";
        for (TableMdId col : pkKey.values()) {
            sb.append(s).append(col.getName());
            if (s.isEmpty()) s = ",";
        }
        sb.append(')');

        if (match != null)
            sb.append(" MATCH").append(match);
        if (deleteRule != null)
            sb.append(" ON DELETE ").append(deleteRule);
        if (updateRule != null)
            sb.append(" ON UPDATE ").append(updateRule);

        return sb.toString();
    }

}

