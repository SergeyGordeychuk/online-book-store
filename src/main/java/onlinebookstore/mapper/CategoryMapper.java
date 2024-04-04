package onlinebookstore.mapper;

import onlinebookstore.config.MapperConfig;
import onlinebookstore.dto.category.CategoryRequestDto;
import onlinebookstore.dto.category.CategoryDto;
import onlinebookstore.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryRequestDto requestDto);
}
