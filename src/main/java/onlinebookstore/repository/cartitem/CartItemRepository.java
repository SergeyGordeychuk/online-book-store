package onlinebookstore.repository.cartitem;

import java.util.Optional;
import onlinebookstore.model.Book;
import onlinebookstore.model.CartItem;
import onlinebookstore.model.ShoppingCart;
import onlinebookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("FROM CartItem ci "
            + "JOIN FETCH ci.book "
            + "WHERE ci.shoppingCart= :shoppingCart AND ci.book= :book")
    CartItem findByShoppingCartAndBook(ShoppingCart shoppingCart, Book book);

    @Query("FROM CartItem ci "
            + "JOIN FETCH ci.shoppingCart "
            + "WHERE ci.id= :id AND ci.shoppingCart.user= :user")
    Optional<CartItem> findByIdAndUser(Long id, User user);
}
