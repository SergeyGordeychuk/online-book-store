package onlinebookstore.service.category;

import java.util.List;
import onlinebookstore.dto.category.CategoryRequestDto;
import onlinebookstore.dto.category.CategoryDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryRequestDto requestDto);

    CategoryDto update(Long id, CategoryRequestDto requestDto);

    void delete(Long id);
}
