package ua.com.nov.model.entity;

public interface MetaData {
    String getName();

    String getViewName();

    String getType();

    Optional<?> getOptions();

    String getCreateStmtDefinition(String conflictOption);
}
