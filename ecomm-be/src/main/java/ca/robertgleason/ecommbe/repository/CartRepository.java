package ca.robertgleason.ecommbe.repository;


import ca.robertgleason.ecommbe.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
