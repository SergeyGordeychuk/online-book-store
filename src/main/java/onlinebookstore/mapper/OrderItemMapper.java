package onlinebookstore.mapper;

import java.util.List;
import java.util.Set;
import onlinebookstore.config.MapperConfig;
import onlinebookstore.dto.orderitem.OrderItemDto;
import onlinebookstore.model.CartItem;
import onlinebookstore.model.Order;
import onlinebookstore.model.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = {CartItemMapper.class})
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", source = "book.price")
    OrderItem toModel(CartItem cartItem, @Context Order order);

    @AfterMapping
    default void setOrder(@MappingTarget OrderItem orderItem, @Context Order order) {
        orderItem.setOrder(order);
    }

    Set<OrderItem> toModelFromCartItem(Set<CartItem> cartItems, @Context Order order);

    List<OrderItemDto> mapToListOrderItems(Set<OrderItem> orderItems);
}
