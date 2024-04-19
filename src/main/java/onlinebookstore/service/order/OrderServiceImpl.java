package onlinebookstore.service.order;

import java.util.List;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.order.OrderDto;
import onlinebookstore.dto.order.OrderShippingAddressRequestDto;
import onlinebookstore.dto.order.OrderStatusRequestDto;
import onlinebookstore.dto.orderitem.OrderItemDto;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.mapper.OrderMapper;
import onlinebookstore.model.Order;
import onlinebookstore.model.ShoppingCart;
import onlinebookstore.model.User;
import onlinebookstore.repository.order.OrderRepository;
import onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import onlinebookstore.service.orderitem.OrderItemService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemService orderItemService;

    @Override
    @Transactional
    public OrderDto createOrder(OrderShippingAddressRequestDto requestDto, User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user).orElseThrow(() ->
                new EntityNotFoundException("Can't find shopping cart by user: " + user));
        Order order = orderMapper.toModel(requestDto, shoppingCart.getCartItems(), user);
        orderRepository.save(order);
        orderItemService.addOrderItems(order, shoppingCart.getCartItems());
        shoppingCartRepository.delete(shoppingCart);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders(User user, Pageable pageable) {
        return orderMapper.mapToListOrderDto(
                orderRepository.findAllByUser(user, pageable).getContent());
    }

    @Override
    public List<OrderItemDto> getAllOrderItemsByOrderId(Long orderId,
                                                        User user,
                                                        Pageable pageable) {
        Order order = orderRepository.findOrderByIdAndUser(orderId, user).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id : " + orderId
                        + " and by user: " + user)
        );
        return orderItemService.getAllOrderItems(order);
    }

    @Override
    public OrderItemDto getOrderItemByOrderIdAndItemId(Long orderId,
                                                       Long itemId,
                                                       User user) {
        Order order = orderRepository.findOrderByIdAndUser(orderId, user).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id : " + orderId
                        + " and by user: " + user)
        );
        return orderItemService.getOrderItemByItemId(itemId, order);
    }

    @Override
    public void updateStatus(Long orderId, OrderStatusRequestDto requestDto) {
        orderRepository.updateStatus(orderId, requestDto.status());
    }

    @Override
    public void delete(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
