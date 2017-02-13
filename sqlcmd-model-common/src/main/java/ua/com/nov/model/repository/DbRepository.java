package ua.com.nov.model.repository;

import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabaseID;

import java.util.HashMap;
import java.util.Map;

public class DbRepository {
    private static DbRepository uniqeDbRepository;
    private Map<DatabaseID, Database> repository = new HashMap<>();

    private DbRepository() {
    }

    public static DbRepository getInstance() {
        if (uniqeDbRepository == null){
            uniqeDbRepository = new DbRepository();
        }
        return uniqeDbRepository;
    }

    public Database removeDb(Database db) {
        return repository.remove(db.getPk());
    }

    public Database addDb(Database db) {
        if (!repository.containsKey(db.getPk())) {
            repository.put(db.getPk(), db);
        }
        return repository.get(db.getPk());
    }

    public Database getDb(DatabaseID pk) {
        Database db = repository.get(pk);
        if (db == null) {
            throw new IllegalArgumentException(
                    String.format("Database %s with user %s doesn't exist in database repository",
                            pk.getDbUrl(), pk.getUserName()));
        }
        return db;
    }

}
