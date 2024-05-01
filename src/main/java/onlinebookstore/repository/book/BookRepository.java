package onlinebookstore.repository.book;

import java.util.List;
import onlinebookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("SELECT b FROM Book b JOIN FETCH b.categories bc  WHERE bc.id= :categoryId ")
    List<Book> findAllByCategoryId(@Param("categoryId") Long categoryId);
}
