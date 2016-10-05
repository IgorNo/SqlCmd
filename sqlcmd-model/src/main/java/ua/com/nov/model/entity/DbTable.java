package ua.com.nov.model.entity;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbTable {
    private DataBase db;
    private String schemaName;
    private Column[] columns;

    private static class Column {
        private final String colName;
        private final String colTypeName;
        private final int colScale;
        private final int colPresicion;


        public Column(String colName, String colTypeName, int colScale, int colPresicion) {
            this.colName = colName;
            this.colTypeName = colTypeName;
            this.colScale = colScale;
            this.colPresicion = colPresicion;
        }

        public String getColName() {
            return colName;
        }

        public String getColTypeName() {
            return colTypeName;
        }

        public int getColScale() {
            return colScale;
        }

        public int getColPresicion() {
            return colPresicion;
        }
    }

    public DbTable(DataBase db, ResultSetMetaData metaData) throws SQLException {
        this.db = db;
        this.schemaName = metaData.getSchemaName(1);
        columns = new Column[metaData.getColumnCount()];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = new Column(
                    metaData.getColumnName(i+1),
                    metaData.getColumnTypeName(i+1),
                    metaData.getScale(i+1),
                    metaData.getPrecision(i+1)
            );
        }
    }

    public DataBase getDb() {
        return db;
    }

    public int getNumberColumns() {
        return columns.length;
    }
    
    public String getColumnName(int index) {
        return columns[index].getColName();
    }
}
