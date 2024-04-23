package onlinebookstore.service.order;

import java.util.List;
import onlinebookstore.dto.order.OrderDto;
import onlinebookstore.dto.order.OrderShippingAddressRequestDto;
import onlinebookstore.dto.order.OrderStatusRequestDto;
import onlinebookstore.dto.orderitem.OrderItemDto;
import onlinebookstore.model.User;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto createOrder(OrderShippingAddressRequestDto requestDto, User user);

    List<OrderDto> getAllOrders(User user, Pageable pageable);

    List<OrderItemDto> getAllOrderItemsByOrderId(Long orderId, User user, Pageable pageable);

    OrderItemDto getOrderItemByOrderIdAndItemId(Long orderId, Long itemId, User user);

    void updateStatus(Long orderId, OrderStatusRequestDto requestDto);

    void delete(Long orderId);
}
