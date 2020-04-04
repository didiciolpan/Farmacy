package Repository;

import Domain.Entity;

import java.security.KeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.max;

public class InMemoryRepository<T extends Entity> implements IRepositiory<T> {

    private Map<Integer, T> storage = new HashMap<>();
    private int currentId;

    public T findById(int id) {
        return storage.get(id);
    }

    @Override
    public void create(T entity) throws KeyException {
        // entity.setId(storage.size() + 1);
//        if (storage.size() == 0) {
//            entity.setId(0);
//        } else {
//            // TODO: don't use max to prevent id reusage.
//            // TODO: Use a private field currentId in the repository class
//            entity.setId(max(storage.keySet()) + 1);
//        }
//
//        if (storage.containsKey(entity.getId())) {
//            throw new KeyException("The entry ID already exists!");
//        }
//        storage.put(entity.getId(), entity);

        if (entity.getId() == -1){
            entity.setId(currentId++);
        }
        if (storage.containsKey(entity.getId())) {
          throw new KeyException("The entry ID already exists!");
            }
        storage.put(entity.getId(), entity);

    }

    @Override
    public T read(int idEntity) {
        return storage.get(idEntity);
    }

    @Override
    public List<T> getAll() {
        List<T> results = new ArrayList<>();
        for (T entity : storage.values()) {
            results.add(entity);
        }
        return results;
    }

    @Override
    public void update(T entity) throws KeyException {
        if (!storage.containsKey(entity.getId())) {
            throw new KeyException("The entry ID does not exist!");
        }
        storage.put(entity.getId(), entity);
    }

    @Override
    public void remove(int idEntity) throws KeyException {
        if (!storage.containsKey(idEntity)) {
            throw new KeyException("The entry ID does not exist!");
        }
        storage.remove(idEntity);
    }

}
