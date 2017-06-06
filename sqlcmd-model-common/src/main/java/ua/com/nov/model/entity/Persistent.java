package ua.com.nov.model.entity;

public interface Persistent {
    String getName();

    String getViewName();

    Optional<?> getOptions();

    String getCreateStmtDefinition(String conflictOption);
}
