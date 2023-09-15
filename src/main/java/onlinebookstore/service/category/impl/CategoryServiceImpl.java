package onlinebookstore.service.category.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.category.CategoryDto;
import onlinebookstore.dto.category.CreateCategoryRequestDto;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.mapper.CategoryMapper;
import onlinebookstore.model.Category;
import onlinebookstore.repository.category.CategoryRepository;
import onlinebookstore.service.category.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Can't find category by id:" + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto requestDto) {
        Category category = categoryMapper.toModel(requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto requestDto) {
        Category category = categoryMapper.toModel(requestDto);
        category.setId(id);
        if (categoryRepository.existsById(category.getId())) {
            categoryRepository.save(category);
        } else {
            throw new EntityNotFoundException("Can't update category " + category);
        }
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
