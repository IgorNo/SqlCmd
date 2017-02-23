package ua.com.nov.model.repository;

import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabaseId;

import java.util.HashMap;
import java.util.Map;

public class DbRepository {
    private static DbRepository uniqeDbRepository;
    private static Map<DatabaseId, Database> repository = new HashMap<>();

    private DbRepository() {
    }

//    public static DbRepository getInstance() {
//        if (uniqeDbRepository == null){
//            uniqeDbRepository = new DbRepository();
//        }
//        return uniqeDbRepository;
//    }

    public Database removeDb(Database db) {
        return repository.remove(db.getId());
    }

    public static Database addDb(Database db) {
        if (!repository.containsKey(db.getId())) {
            repository.put(db.getId(), db);
        }
        return repository.get(db.getId());
    }

    public static Database getDb(DatabaseId pk) {
        Database db = repository.get(pk);
        if (db == null) {
            throw new IllegalArgumentException(
                    String.format("Database %s with user %s doesn't exist in database repository",
                            pk.getDbUrl(), pk.getUserName()));
        }
        return db;
    }

}
