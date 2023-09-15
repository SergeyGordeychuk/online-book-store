package onlinebookstore.mapper;

import onlinebookstore.config.MapperConfig;
import onlinebookstore.dto.category.CategoryDto;
import onlinebookstore.dto.category.CreateCategoryRequestDto;
import onlinebookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toModel(CreateCategoryRequestDto requestDto);
}
