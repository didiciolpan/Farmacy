package Repository;

import Domain.Entity;

import java.security.KeyException;
import java.util.List;

public interface IRepositiory<T extends Entity> {   // T = orice obiect care extinde entity

    void create(T entity) throws KeyException;

    T findById(int id);

    T read(int idEntity);

    List<T> getAll();

    void update(T entity) throws KeyException;

    void remove(int idEntity) throws KeyException;
}
