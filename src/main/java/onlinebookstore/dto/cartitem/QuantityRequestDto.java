package onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class QuantityRequestDto {
    @Positive
    @NotNull
    Integer quantity;
}
