package onlinebookstore.service;

import java.util.List;

import onlinebookstore.dto.BookDto;
import onlinebookstore.dto.CreateBookRequestDto;
import onlinebookstore.model.Book;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    List<BookDto> getAllByTitle(String title);

    List<BookDto> findAll();
}
