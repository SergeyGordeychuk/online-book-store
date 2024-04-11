package onlinebookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE books SET is_deleted = TRUE WHERE id = ?")
@Where(clause = "is_deleted=false")
@Table(name = "books")
@NoArgsConstructor
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
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "books_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    public Book(Long id) {
        this.id = id;
    }
}
