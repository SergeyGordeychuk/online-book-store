package onlinebookstore.repository;

import java.util.List;
import java.util.Optional;
import onlinebookstore.model.Book;

public interface BookRepository {

    Book save(Book book);

    Optional<Book> findById(Long id);

    List<Book> findAllByTitle(String title);

    List<Book> findAll();
}
