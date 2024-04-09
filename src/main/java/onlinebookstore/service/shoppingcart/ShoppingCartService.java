package onlinebookstore.service.shoppingcart;

import onlinebookstore.dto.cartitem.CartItemRequestDto;
import onlinebookstore.dto.cartitem.QuantityRequestDto;
import onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import onlinebookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart(User user);

    ShoppingCartResponseDto saveCartItemToShoppingCart(
            CartItemRequestDto requestDto, User user);

    ShoppingCartResponseDto update(Long id, QuantityRequestDto requestDto, User user);

    void delete(Long id, User user);
}
