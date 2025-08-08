package ca.robertgleason.ecommbe.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String image;
    
    private String productDescription;
    private Integer quantity;
    private Double specialPrice;
    private Double discount;
    private Double price;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


}
