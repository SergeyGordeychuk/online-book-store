package onlinebookstore.service;

import onlinebookstore.dto.BookDto;
import onlinebookstore.dto.BookSearchParameters;
import onlinebookstore.dto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters params);
}
