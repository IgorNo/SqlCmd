package ua.com.nov.model.entity;

public interface Unique {
    <K extends Persistent<V>, V> K getId();
}
