package onlinebookstore.dto.order;

import jakarta.validation.constraints.NotBlank;

public record OrderShippingAddressRequestDto(
        @NotBlank
        String shippingAddress
) {
}
