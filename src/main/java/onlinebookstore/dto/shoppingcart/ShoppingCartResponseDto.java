package onlinebookstore.dto.shoppingcart;

import java.util.Set;
import lombok.Data;
import onlinebookstore.dto.cartitem.CartItemResponseDto;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItemsDto;
}
