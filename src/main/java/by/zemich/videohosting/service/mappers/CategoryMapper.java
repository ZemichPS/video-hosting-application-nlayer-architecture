package by.zemich.videohosting.service.mappers;

import by.zemich.videohosting.dao.entities.Category;
import by.zemich.videohosting.dao.entities.User;
import by.zemich.videohosting.core.models.dto.request.CategoryData;
import by.zemich.videohosting.core.models.dto.response.CategoryShortRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category categoryDataToNewCategory(CategoryData categoryData);

    void categoryDataToExistingCategory(CategoryData categoryData, @MappingTarget Category category);

    CategoryShortRepresentation categoryToShortRepresentation(Category category);
}
