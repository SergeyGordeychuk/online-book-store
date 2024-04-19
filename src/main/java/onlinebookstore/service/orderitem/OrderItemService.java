package onlinebookstore.service.orderitem;

import java.util.List;
import java.util.Set;
import onlinebookstore.dto.orderitem.OrderItemDto;
import onlinebookstore.model.CartItem;
import onlinebookstore.model.Order;

public interface OrderItemService {
    void addOrderItems(Order order, Set<CartItem> cartItems);

    List<OrderItemDto> getAllOrderItems(Order order);

    OrderItemDto getOrderItemByItemId(Long itemId, Order order);
}
