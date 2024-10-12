package by.zemich.videohosting.service;

import by.zemich.videohosting.dao.entities.Category;
import by.zemich.videohosting.dao.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryCrudService {
    private final CategoryRepository categoryRepository;

    public CategoryCrudService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }


    public Optional<Category> findById(UUID id) {
        return categoryRepository.findById(id);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteById(UUID categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public boolean existsById(UUID id) {
        return categoryRepository.existsById(id);
    }
}
