package onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequestDto {
    private Long bookId;
    @Positive
    private int quantity;
}
