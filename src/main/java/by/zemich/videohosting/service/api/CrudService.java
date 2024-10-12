package by.zemich.videohosting.service.api;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, I> {
    Optional<T> findById(I id);

    List<T> findAll();

    T save(T entity);

    void deleteById(I id);

    boolean existsById(I id);
}
