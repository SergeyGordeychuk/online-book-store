package onlinebookstore.service.shoppingcart;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.cartitem.CartItemRequestDto;
import onlinebookstore.dto.cartitem.QuantityRequestDto;
import onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.exception.UnauthorizedAccessException;
import onlinebookstore.mapper.ShoppingCartMapper;
import onlinebookstore.model.Book;
import onlinebookstore.model.CartItem;
import onlinebookstore.model.ShoppingCart;
import onlinebookstore.model.User;
import onlinebookstore.repository.book.BookRepository;
import onlinebookstore.repository.cartitem.CartItemRepository;
import onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartResponseDto getShoppingCart(User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user).orElseThrow(
                () -> new EntityNotFoundException("Can't find shoppingCart by user: " + user)
        );
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto saveCartItemToShoppingCart(
            CartItemRequestDto requestDto, User user) {
        Optional<ShoppingCart> shoppingCartByUser =
                shoppingCartRepository.findByUser(user);
        if (!shoppingCartByUser.isEmpty()) {
            return addCartItem(shoppingCartByUser.get(), requestDto);
        } else {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            shoppingCartRepository.save(shoppingCart);
            return addCartItem(shoppingCart, requestDto);
        }
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto update(Long id, QuantityRequestDto requestDto, User user) {
        CartItem cartItem = checkItemByUser(id, user);
        cartItem.setQuantity(cartItem.getQuantity() + requestDto.getQuantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(cartItem.getShoppingCart());
    }

    @Transactional
    @Override
    public void delete(Long id, User user) {
        CartItem cartItem = checkItemByUser(id, user);
        cartItemRepository.delete(cartItem);
    }

    private ShoppingCartResponseDto addCartItem(ShoppingCart shoppingCart,
                                                CartItemRequestDto requestDto) {
        Book book = bookRepository.findById(requestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find book by id: " + requestDto.getBookId())
        );
        CartItem byShoppingCartAndBook = cartItemRepository
                .findByShoppingCartAndBook(shoppingCart, book);
        if (byShoppingCartAndBook == null) {
            CartItem cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setBook(book);
            cartItem.setQuantity(requestDto.getQuantity());
            cartItemRepository.save(cartItem);
            shoppingCart.getCartItems().add(cartItem);
        } else {
            byShoppingCartAndBook.setQuantity(byShoppingCartAndBook.getQuantity()
                    + requestDto.getQuantity());
            cartItemRepository.save(byShoppingCartAndBook);
        }
        return shoppingCartMapper.toDto(shoppingCart);
    }

    private CartItem checkItemByUser(Long id, User user) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find cart item by id: " + id)
        );
        if (!Objects.equals(cartItem.getShoppingCart().getUser().getId(), user.getId())) {
            throw new UnauthorizedAccessException(
                    "User is not authorized to access this shopping cart item");
        }
        return cartItem;
    }
}
