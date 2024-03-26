package onlinebookstore.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateBookRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String author;
    @NotNull
    @Column(unique = true)
    @NotNull
    private String isbn;
    @NotNull
    @Min(0)
    private BigDecimal price;
    private String description;
    private String coverImage;
}
