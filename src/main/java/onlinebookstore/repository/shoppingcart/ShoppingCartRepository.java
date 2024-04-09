package onlinebookstore.repository.shoppingcart;

import java.util.Optional;
import onlinebookstore.model.ShoppingCart;
import onlinebookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query(" from ShoppingCart sc " +
            "join fetch sc.cartItems ci " +
            "join fetch ci.book " +
            "where sc.user= :user ")
    Optional<ShoppingCart> findByUser(User user);
}
