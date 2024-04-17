package onlinebookstore.controller;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.order.OrderDto;
import onlinebookstore.dto.order.OrderShippingAddressRequestDto;
import onlinebookstore.dto.order.OrderStatusRequestDto;
import onlinebookstore.dto.orderitem.OrderItemDto;
import onlinebookstore.model.User;
import onlinebookstore.service.order.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public OrderDto createOrder(@RequestBody @Valid OrderShippingAddressRequestDto requestDto,
                                Authentication authentication) {
        User user = getUser(authentication);
        return orderService.createOrder(requestDto, user);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(description = "Get all orders with "
            + "pagination and ability to sort ")
    public List<OrderDto> getAllOrders(Pageable pageable, Authentication authentication) {
        User user = getUser(authentication);
        return orderService.getAllOrders(user, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items")
    @Operation(description = "Get all orderItems by order with "
            + "pagination and ability to sort ")
    public List<OrderItemDto> getAllOrderItemsByOrderId(@PathVariable Long orderId,
                                                        Authentication authentication,
                                                        Pageable pageable) {
        User user = getUser(authentication);
        return orderService.getAllOrderItemsByOrderId(orderId, user, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(description = "Get orderItem by 'orderId' and 'itemId'")
    OrderItemDto getOrderItemByIdByOrderId(@PathVariable Long orderId,
                                           @PathVariable Long itemId,
                                           Authentication authentication
    ) {
        User user = getUser(authentication);
        return orderService.getOrderItemByOrderIdAndItemId(orderId, itemId, user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{orderId}")
    @Operation(description = "Patch order status by order id")
    public void updateStatus(@PathVariable Long orderId,
                             @RequestBody @Valid OrderStatusRequestDto requestDto) {
        orderService.updateStatus(orderId, requestDto);
    }

    private User getUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
