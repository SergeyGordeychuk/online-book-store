package onlinebookstore.service.book;

import java.util.List;
import onlinebookstore.dto.book.BookDto;
import onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import onlinebookstore.dto.book.BookSearchParameters;
import onlinebookstore.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDtoWithoutCategoryIds save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters params, Pageable pageable);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long id);
}
