package onlinebookstore.service;

import java.util.List;
import onlinebookstore.dto.BookDto;
import onlinebookstore.dto.CreateBookRequestDto;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    List<BookDto> getAllByTitle(String title);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    void deleteById(Long id);
}
