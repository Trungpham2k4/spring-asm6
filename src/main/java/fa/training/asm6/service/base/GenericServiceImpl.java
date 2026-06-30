package fa.training.asm6.service.base;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class GenericServiceImpl<T, ID> implements GenericService<T,ID> {

    protected JpaRepository<T, ID> repository;

    public GenericServiceImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public T findById(ID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }
}
