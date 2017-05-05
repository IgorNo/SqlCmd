package ua.com.nov.model.entity;

import ua.com.nov.model.entity.metadata.database.Database;

import java.util.List;
import java.util.Map;

public interface Optional<E> {
    String getCreateOptionsDefinition();

    List<String> getUpdateOptionsDefinition();

    Map<String, String> getOptionsMap();

    String getOption(String name);

    Class<? extends Database> getDbClass();
}
