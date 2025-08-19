package ca.robertgleason.ecommbe.repository;

import ca.robertgleason.ecommbe.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
