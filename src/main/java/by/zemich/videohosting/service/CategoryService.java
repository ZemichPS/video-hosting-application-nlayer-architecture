package by.zemich.videohosting.service;

import by.zemich.videohosting.dao.entities.Category;
import by.zemich.videohosting.dao.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Transactional(readOnly = true)
    public Flux<Category> findAll(){
        return categoryRepository.findAll();
    }

    public Mono<Category> findById(UUID id){
        return categoryRepository.findById(id);
    }

    public Mono<Category> save(Category category){
        return categoryRepository.save(category);
    }


}
