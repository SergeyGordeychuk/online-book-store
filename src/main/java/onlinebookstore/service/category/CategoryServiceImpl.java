package onlinebookstore.service.category;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.category.CategoryRequestDto;
import onlinebookstore.dto.category.CategoryDto;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.mapper.CategoryMapper;
import onlinebookstore.model.Category;
import onlinebookstore.repository.category.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find by id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CategoryRequestDto requestDto) {
        Category categoryModel = categoryMapper.toEntity(requestDto);
        return categoryMapper.toDto(categoryRepository.save(categoryModel));
    }

    @Override
    public CategoryDto update(Long id, CategoryRequestDto requestDto) {
        Category category = categoryMapper.toEntity(requestDto);
        category.setId(id);
        if (categoryRepository.existsById(id)) {
            categoryRepository.save(category);
        } else {
            throw new NoSuchElementException("Can't update category: " + category);
        }
        return categoryMapper.toDto(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
