package onlinebookstore.repository.book;

import java.util.List;
import onlinebookstore.model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;


    @Test
    @DisplayName("""
            Find all books by categoryId
            """)
    @Sql(scripts = {
            "classpath:database/book/remove-all-from-books-with-categories-table.sql",
            "classpath:database/book/add-three-books-to-books-with-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove-all-from-books-with-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_TwoActualCategories_ReturnTwoBooks() {
        Long categoryId = 3L;
        int expected = 2;
        List<Book> booksByCategoryId = bookRepository.findAllByCategoryId(categoryId);
        Assertions.assertEquals(expected, booksByCategoryId.size());
    }
}
