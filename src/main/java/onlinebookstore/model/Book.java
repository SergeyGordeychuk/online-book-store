package onlinebookstore.model;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@SQLDelete(sql = "UPDATE books SET is_deleted = TRUE WHERE id = ?")
@Where(clause = "is_deleted=false")
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "titles",nullable = false)
    private String title;
    @Column(name = "authors",nullable = false)
    private String author;
    @Column(unique = true,nullable = false)
    private String isbn;
    @Column(nullable = false)
    private BigDecimal price;
    private String description;
    private String coverImage;
    @Column(nullable = false,name = "is_deleted")
    private boolean isDeleted = false;
}
