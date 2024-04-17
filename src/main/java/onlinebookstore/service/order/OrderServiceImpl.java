package onlinebookstore.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.order.OrderDto;
import onlinebookstore.dto.order.OrderShippingAddressRequestDto;
import onlinebookstore.dto.order.OrderStatusRequestDto;
import onlinebookstore.dto.orderitem.OrderItemDto;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.mapper.OrderItemMapper;
import onlinebookstore.mapper.OrderMapper;
import onlinebookstore.model.Order;
import onlinebookstore.model.OrderItem;
import onlinebookstore.model.ShoppingCart;
import onlinebookstore.model.User;
import onlinebookstore.repository.order.OrderRepository;
import onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final LocalDateTime DATE_TIME = LocalDateTime.now();

    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDto createOrder(OrderShippingAddressRequestDto requestDto, User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user).orElseThrow(() ->
                new EntityNotFoundException("Can't find shopping cart by user: " + user));
        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(orderItemMapper::toModelFromCartItem)
                .collect(Collectors.toSet());
        BigDecimal total = shoppingCart.getCartItems().stream()
                .map(ci -> ci.getBook().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setShippingAddress(requestDto.shippingAddress());
        order.setOrderDate(DATE_TIME);
        order.setTotal(total);
        order = orderRepository.save(order);
        addOrderItems(order, orderItems);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getAllOrders(User user, Pageable pageable) {
        return orderRepository.findAllByUser(user, pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemDto> getAllOrderItemsByOrderId(Long orderId,
                                                        User user,
                                                        Pageable pageable) {
        return orderRepository.getAllOrderItemsByOrderId(orderId, user, pageable).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItemByOrderIdAndItemId(Long orderId,
                                                       Long itemId,
                                                       User user) {
        Order order = orderRepository.findOrderByIdAndUser(orderId, user).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id : " + orderId
                + " and by user: " + user)
        );
        OrderItem orderItem = order.getOrderItems().stream()
                .filter(oi -> Objects.equals(oi.getId(), itemId))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find item by id: " + itemId));
        return orderItemMapper.toDto(orderItem);
    }

    @Override
    public void updateStatus(Long orderId, OrderStatusRequestDto requestDto) {
        orderRepository.updateStatus(orderId, requestDto.status());
    }

    private void addOrderItems(Order order, Set<OrderItem> orderItems) {
        Set<OrderItem> orderItemSet = orderItems.stream()
                .peek(oi -> oi.setOrder(order)).collect(Collectors.toSet());
        order.setOrderItems(orderItemSet);
    }
}
