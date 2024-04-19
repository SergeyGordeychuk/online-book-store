package onlinebookstore.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import onlinebookstore.config.MapperConfig;
import onlinebookstore.dto.order.OrderDto;
import onlinebookstore.dto.order.OrderShippingAddressRequestDto;
import onlinebookstore.model.CartItem;
import onlinebookstore.model.Order;
import onlinebookstore.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    OrderDto toDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shippingAddress", source = "requestDto.shippingAddress")
    Order toModel(OrderShippingAddressRequestDto requestDto,
                  Set<CartItem> cartItems,
                  User user);

    @AfterMapping
    default void setUser(@MappingTarget Order order, User user) {
        order.setUser(user);
    }

    @AfterMapping
    default void setTotal(@MappingTarget Order order, Set<CartItem> cartItems) {
        cartItems.stream()
                .map(cartItem -> cartItem.getBook().getPrice().multiply(
                        BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal::add)
                .ifPresent(order::setTotal);
    }

    List<OrderDto> mapToListOrderDto(List<Order> orders);
}
