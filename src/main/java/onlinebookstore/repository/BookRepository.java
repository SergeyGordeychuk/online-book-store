package onlinebookstore.repository;

import java.util.List;
import onlinebookstore.model.Book;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();
}
