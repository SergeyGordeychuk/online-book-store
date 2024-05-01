package onlinebookstore.service.category;

import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import onlinebookstore.dto.category.CategoryDto;
import onlinebookstore.dto.category.CategoryRequestDto;
import onlinebookstore.mapper.CategoryMapper;
import onlinebookstore.model.Category;
import onlinebookstore.repository.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    private static Category category;
    private static CategoryDto categoryDto;
    private static CategoryRequestDto categoryRequestDto;

    @BeforeAll
    static void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Novel");

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Novel");
        categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("Novel");
    }

    @Test
    @DisplayName("""
            Get category by id
            """)
    void getCategory_WithValidId_ReturnCategory() {
        Long categoryId = 1L;
        CategoryDto expected = categoryDto;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        CategoryDto actual = categoryService.getById(categoryId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Get exception by invalidId 
            """)
    void getCategory_WithInvalidId_ReturnException() {
        Long invalidId = 100L;
        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());
        RuntimeException runtimeException =
                Assertions.assertThrows(RuntimeException.class, () -> categoryService.getById(invalidId));
        String expected = "Can't find by id: " + invalidId;
        String actual = runtimeException.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Save catgory
            """)
    void saveCategory_WithValidCategory_ReturnCategory() {
        Long categoryId = 1L;
        CategoryDto expected = categoryDto;
        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        CategoryDto actual = categoryService.save(categoryRequestDto);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Update category with invalid id
            """)
    void updateCategory_WithInvalidId_ReturnException() {
        Long invalidId = 100L;
        when(categoryRepository.existsById(invalidId)).thenReturn(false);
        NoSuchElementException noSuchElementException =
                Assertions.assertThrows(NoSuchElementException.class,
                        () -> categoryService.update(invalidId, categoryRequestDto));
        String expected = "Can't find category by id: " + invalidId;
        String actual = noSuchElementException.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Update category
            """)
    void updateCategory_WithValidCategory_UpdateCategory() {
        Long categoryId = 1L;
        CategoryDto expected = categoryDto;
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        CategoryDto actual = categoryService.update(categoryId, categoryRequestDto);
        Assertions.assertEquals(expected, actual);
    }
}
