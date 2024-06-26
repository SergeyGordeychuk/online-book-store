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
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
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
    @Transactional
    public CategoryDto update(Long id, CategoryRequestDto requestDto) {
        if (!categoryRepository.existsById(id)) {
            throw new NoSuchElementException("Can't find category by id: " + id);
        }
        Category category = categoryMapper.toEntity(requestDto);
        category.setId(id);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
