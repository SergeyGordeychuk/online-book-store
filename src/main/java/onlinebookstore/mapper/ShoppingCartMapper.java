package onlinebookstore.mapper;

import onlinebookstore.config.MapperConfig;
import onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import onlinebookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItemsDto", source = "cartItems")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);
}
