package ua.com.nov.model.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mappable<V> {
    V rowMap(ResultSet rs) throws SQLException;
}
