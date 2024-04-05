package onlinebookstore.service.book;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.book.BookDto;
import onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import onlinebookstore.dto.book.BookSearchParameters;
import onlinebookstore.dto.book.CreateBookRequestDto;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.mapper.BookMapper;
import onlinebookstore.model.Book;
import onlinebookstore.repository.book.BookRepository;
import onlinebookstore.repository.book.BookSpecificationBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDtoWithoutCategoryIds save(CreateBookRequestDto requestDto) {
        Book bookMapperModel = bookMapper.toEntity(requestDto);
        return bookMapper.toDtoWithoutCategories(bookRepository.save(bookMapperModel));
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find by id: " + id));
        return bookMapper.toDto(book);
    }

    @Transactional
    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        if (!bookRepository.existsById(id)) {
            throw new NoSuchElementException("Can't update book by: " + id);
        }
        Book book = bookMapper.toEntity(requestDto);
        book.setId(id);
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParameters params) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long id) {
        return bookRepository.findAllByCategoryId(id)
                .stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
