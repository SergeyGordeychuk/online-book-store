package onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.cartitem.CartItemRequestDto;
import onlinebookstore.dto.cartitem.QuantityRequestDto;
import onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import onlinebookstore.model.User;
import onlinebookstore.service.shoppingcart.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cart management", description = "Endpoint for managing carts")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(description = "Gets all books with shopping cart")
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        User user = getUser(authentication);
        return shoppingCartService.getShoppingCart(user);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(description = "Save book to shopping cart")
    public ShoppingCartResponseDto saveCartItemToShoppingCart(
            @RequestBody @Valid CartItemRequestDto requestDto, Authentication authentication) {
        User user = getUser(authentication);
        return shoppingCartService.saveCartItemToShoppingCart(requestDto, user);
    }

    @PutMapping("/cart-items/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(description = "Update quantity by cartItemId")
    public ShoppingCartResponseDto update(@PathVariable Long id,
                                          @RequestBody QuantityRequestDto requestDto,
                                          Authentication authentication) {
        User user = getUser(authentication);
        return shoppingCartService.update(id, requestDto, user);
    }

    @DeleteMapping("/cart-items/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(description = "Delete cartItem by cartItemId")
    public void delete(@PathVariable Long id, Authentication authentication) {
        User user = getUser(authentication);
        shoppingCartService.delete(id, user);
    }

    private User getUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
