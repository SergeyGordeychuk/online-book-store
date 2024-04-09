package onlinebookstore.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import onlinebookstore.config.MapperConfig;
import onlinebookstore.dto.cartitem.CartItemResponseDto;
import onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import onlinebookstore.model.CartItem;
import onlinebookstore.model.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItems", source = "cartItems")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);

    @AfterMapping
    default Set<CartItemResponseDto> mapCartItemsDto(Set<CartItem> cartItems) {
        if (cartItems == null) {
            return null;
        }
        return cartItems.stream()
                .map(cartItem -> {
                    CartItemResponseDto cartItemDto = new CartItemResponseDto();
                    cartItemDto.setId(cartItem.getId());
                    cartItemDto.setBookId(cartItem.getBook().getId());
                    cartItemDto.setBookTitle(cartItem.getBook().getTitle());
                    cartItemDto.setQuantity(cartItem.getQuantity());
                    return cartItemDto;
                })
                .collect(Collectors.toSet());
    }
}
