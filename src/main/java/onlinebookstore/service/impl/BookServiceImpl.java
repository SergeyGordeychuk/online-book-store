package onlinebookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.BookDto;
import onlinebookstore.dto.CreateBookRequestDto;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.mapper.BookMapper;
import onlinebookstore.model.Book;
import onlinebookstore.repository.BookRepository;
import onlinebookstore.service.BookService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book bookMapperModel = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(bookMapperModel));
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find by id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> getAllByTitle(String title) {
        return bookRepository.findAllByTitleContainingIgnoreCase(title).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
