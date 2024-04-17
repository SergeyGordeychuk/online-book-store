package onlinebookstore.repository.order;

import java.util.Optional;
import onlinebookstore.model.Order;
import onlinebookstore.model.OrderItem;
import onlinebookstore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("FROM Order o "
            + "JOIN FETCH o.orderItems "
            + "WHERE o.id= :orderId AND o.user= :user")
    Optional<Order> findOrderByIdAndUser(Long orderId, User user);

    @Query("FROM Order o "
            + "JOIN FETCH o.orderItems oi "
            + "JOIN FETCH oi.book "
            + "WHERE o.user= :user")
    Page<Order> findAllByUser(User user, Pageable pageable);

    @Query("SELECT o.orderItems "
            + "FROM Order o "
            + "WHERE o.id= :orderId AND o.user= :user")
    Page<OrderItem> getAllOrderItemsByOrderId(Long orderId, User user, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status= :status WHERE o.id= :orderId")
    void updateStatus(Long orderId, Order.Status status);
}
