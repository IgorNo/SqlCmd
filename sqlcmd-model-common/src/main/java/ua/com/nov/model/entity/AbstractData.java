package ua.com.nov.model.entity;

public class AbstractData<K extends Persistent> implements Unique<K>{
    private K id;

    @Override
    public K getId() {
        return null;
    }

    public static abstract class Builder<K, V extends AbstractData> implements Buildable<V> {
        private K id;
    }
}
