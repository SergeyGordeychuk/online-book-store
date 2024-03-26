package onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.BookDto;
import onlinebookstore.dto.BookSearchParameters;
import onlinebookstore.dto.CreateBookRequestDto;
import onlinebookstore.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Book management", description = "Endpoint for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(description = "Gets all books with "
            + "pagination and ability to sort ")
    public List<BookDto> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(description = "Get book by 'id'")
    public BookDto findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(description = "Update book by 'id'")
    public BookDto update(@PathVariable Long id,
                          @RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.update(id, requestDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Create a new book")
    public BookDto save(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(description = "Delete book by 'id'")
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @GetMapping("/search")
    @Operation(description = "Gets all books with "
            + " sort by price or by title "
            + " or by author in ASC or DESC order")
    public List<BookDto> search(BookSearchParameters params) {
        return bookService.search(params);
    }
}
