package ua.com.nov.model.entity.metadata.table.column;

public class KeyCol {
    private final String name;
    private final String options;

    public KeyCol(String name, String options) {
        this.name = name;
        this.options = options;
    }

    public KeyCol(String name) {
        this(name, null);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyCol)) return false;

        KeyCol column = (KeyCol) o;

        return name.equalsIgnoreCase(column.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(name);
        if (options != null)
            sb.append(' ').append(options);
        return sb.toString();
    }
}