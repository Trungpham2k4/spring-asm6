package fa.training.asm6.service.base;

import java.util.List;

public interface GenericService<T, ID> {
    T save(T entity);
    T findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
}
