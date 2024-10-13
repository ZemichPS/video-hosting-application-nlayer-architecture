package by.zemich.videohosting.service;

import by.zemich.videohosting.dao.entities.Category;
import by.zemich.videohosting.core.models.dto.request.CategoryData;
import by.zemich.videohosting.core.models.dto.response.CategoryShortRepresentation;
import by.zemich.videohosting.core.exceptions.CategoryNotFoundException;
import by.zemich.videohosting.service.crud.CategoryCrudService;
import by.zemich.videohosting.service.mappers.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceFacade {
    private final CategoryCrudService categoryCrudService;

    public CategoryShortRepresentation create(CategoryData data) {
        Category category = CategoryMapper.INSTANCE.categoryDataToNewCategory(data);
        category = categoryCrudService.save(category);
        return CategoryMapper.INSTANCE.categoryToShortRepresentation(category);
    }

    public CategoryShortRepresentation update(UUID categoryId, CategoryData categoryData) {
        return categoryCrudService.findById(categoryId)
                .map(category -> {
                    CategoryMapper.INSTANCE.categoryDataToExistingCategory(categoryData, category);
                    return categoryCrudService.save(category);
                })
                .map(CategoryMapper.INSTANCE::categoryToShortRepresentation)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id %s is nowhere to be found".formatted(categoryId)));
    }

    public CategoryShortRepresentation patch(UUID categoryId, Map<String, Object> valuesMap) {
        return categoryCrudService.findById(categoryId)
                .map(category -> {
                    if (valuesMap.containsKey("name")) {
                        category.setName((String) valuesMap.get("name"));
                    }
                    return categoryCrudService.save(category);
                })
                .map(CategoryMapper.INSTANCE::categoryToShortRepresentation)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id %s is nowhere to be found".formatted(categoryId)));
    }

    public void delete(UUID categoryId) {
        if (!categoryCrudService.existsById(categoryId))
            throw new CategoryNotFoundException("Category with id %s is nowhere to be found".formatted(categoryId));
        categoryCrudService.deleteById(categoryId);
    }

    public CategoryShortRepresentation findById(UUID categoryId) {
        return categoryCrudService.findById(categoryId)
                .map(CategoryMapper.INSTANCE::categoryToShortRepresentation)
                .orElseThrow(()-> new CategoryNotFoundException("Category with id %s is nowhere to be found".formatted(categoryId)));
    }
}
