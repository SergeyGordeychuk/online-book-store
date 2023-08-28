package onlinebookstore.repository;

import java.util.List;
import onlinebookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByTitleContainingIgnoreCase(String title);
}
