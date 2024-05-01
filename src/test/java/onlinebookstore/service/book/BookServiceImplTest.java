package onlinebookstore.service.book;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import onlinebookstore.dto.book.BookDto;
import onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import onlinebookstore.dto.book.CreateBookRequestDto;
import onlinebookstore.mapper.BookMapper;
import onlinebookstore.model.Book;
import onlinebookstore.model.Category;
import onlinebookstore.repository.book.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;
    private static Book book;
    private static BookDto bookDto;
    private static BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds;
    private static CreateBookRequestDto requestDto;

    @BeforeAll
    static void setUp() {
        requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("author");
        requestDto.setTitle("title");
        requestDto.setIsbn("123");
        requestDto.setPrice(BigDecimal.valueOf(100));

        book = new Book();
        book.setId(1L);
        book.setAuthor(requestDto.getAuthor());
        book.setTitle(requestDto.getTitle());
        book.setIsbn(requestDto.getIsbn());
        book.setPrice(requestDto.getPrice());
        book.setCategories(Set.of(new Category(1L)));

        bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds.setId(1L);
        bookDtoWithoutCategoryIds.setAuthor("author");
        bookDtoWithoutCategoryIds.setTitle("title");
        bookDtoWithoutCategoryIds.setIsbn("123");
        bookDtoWithoutCategoryIds.setPrice(BigDecimal.valueOf(100));

        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setAuthor(book.getAuthor());
        bookDto.setTitle(book.getTitle());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setCategories(Set.of(1L));
    }

    @Test
    @DisplayName("""
            Update book
            """)
    void updateBook_WithCorrectParameters_ShouldUpdateBook() {
        Long id = 1L;
        when(bookRepository.existsById(id)).thenReturn(true);
        when(bookMapper.toEntity(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto expected = bookDto;
        BookDto actual = bookService.update(id, requestDto);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Get exception from update book with invalid id
            """)
    void updateBook_WithInvalidId_ReturnsException() {
        Long invalidId = 1L;
        when(bookRepository.existsById(invalidId)).thenReturn(false);
        RuntimeException runtimeException = Assertions.assertThrows(
                RuntimeException.class, () -> bookService.update(invalidId, requestDto));
        String actual = runtimeException.getMessage();
        String expected = "Can't update book by: " + invalidId;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Create book
            """)
    void createBook_WithValidBook_ReturnsBookDtoWithoutCategoryIds() {
        Long bookId = 1L;
        BookDtoWithoutCategoryIds expected = bookDtoWithoutCategoryIds;
        expected.setId(bookId);
        book.setId(bookId);
        when(bookMapper.toEntity(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategoryIds);

        BookDtoWithoutCategoryIds actual = bookService.save(requestDto);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Get book by id
            """)
    void getBook_WithValidBookId_ReturnsBook() {
        Long id = 1L;
        BookDto expected = bookDto;

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.findById(id);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Get exception by invalidId 
            """)
    void getBook_WithInvalidBookId_ReturnsThrowException() {
        Long invalidId = 11L;
        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());
        RuntimeException runtimeException =
                Assertions.assertThrows(
                        RuntimeException.class, () -> bookService.findById(invalidId));
        String expected = "Can't find by id: " + invalidId;
        String actual = runtimeException.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Find book by category
            """)
    void findBook_WithValidBookCategory_ReturnsBook() {
        Long categoryId = 1L;
        List<BookDtoWithoutCategoryIds> expected = new ArrayList<>();
        expected.add(bookDtoWithoutCategoryIds);
        when(bookRepository.findAllByCategoryId(categoryId)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategoryIds);
        List<BookDtoWithoutCategoryIds> actual = bookService.findAllByCategoryId(categoryId);
        Assertions.assertEquals(expected, actual);
    }
}
