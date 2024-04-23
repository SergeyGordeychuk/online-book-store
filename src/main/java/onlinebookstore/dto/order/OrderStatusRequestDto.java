package onlinebookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import onlinebookstore.model.Order;

public record OrderStatusRequestDto(
        @NotNull
        Order.Status status
) {
}
