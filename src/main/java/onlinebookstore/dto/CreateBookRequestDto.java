package onlinebookstore.dto;

import jakarta.persistence.Column;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    private String title;
    private String author;
    @Column(unique = true)
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
}
