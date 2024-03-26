package onlinebookstore.service.category;

import java.util.List;
import onlinebookstore.dto.category.CategoryDto;
import onlinebookstore.dto.category.CreateCategoryRequestDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    public List<CategoryDto> findAll(Pageable pageable);
    public CategoryDto getById(Long id);
    public CategoryDto save(CreateCategoryRequestDto requestDto);
    public CategoryDto update(Long id, CreateCategoryRequestDto requestDto);
    public void deleteById(Long id);
}
