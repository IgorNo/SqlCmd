package ua.com.nov.model.entity;

public interface Persistent {

    String getViewName();

    Optional<?> getOptions();

    String getCreateStmtDefinition(String conflictOption);
}
