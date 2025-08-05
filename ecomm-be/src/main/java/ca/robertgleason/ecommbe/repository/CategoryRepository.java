package ca.robertgleason.ecommbe.repository;

import ca.robertgleason.ecommbe.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {


}
