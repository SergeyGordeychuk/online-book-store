package onlinebookstore.service.orderitem;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.orderitem.OrderItemDto;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.mapper.OrderItemMapper;
import onlinebookstore.model.CartItem;
import onlinebookstore.model.Order;
import onlinebookstore.model.OrderItem;
import onlinebookstore.repository.orderitem.OrderItemRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public void addOrderItems(Order order, Set<CartItem> cartItems) {
        Set<OrderItem> orderItemSet = orderItemMapper.toModelFromCartItem(cartItems, order);
        orderItemRepository.saveAll(orderItemSet);
        order.setOrderItems(orderItemSet);
    }

    @Override
    public List<OrderItemDto> getAllOrderItems(Order order) {
        return orderItemMapper.mapToListOrderItems(order.getOrderItems());
    }

    @Override
    public OrderItemDto getOrderItemByItemId(Long itemId, Order order) {
        OrderItem orderItem = order.getOrderItems().stream()
                .filter(oi -> Objects.equals(oi.getId(), itemId))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find item by id: " + itemId));
        return orderItemMapper.toDto(orderItem);
    }
}
