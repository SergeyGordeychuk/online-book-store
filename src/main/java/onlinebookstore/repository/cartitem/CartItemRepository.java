package onlinebookstore.repository.cartitem;

import onlinebookstore.model.Book;
import onlinebookstore.model.CartItem;
import onlinebookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("from CartItem ci "
            + "join fetch ci.book "
            + "where ci.shoppingCart= :shoppingCart and ci.book= :book")
    CartItem findByShoppingCartAndBook(ShoppingCart shoppingCart, Book book);
}
