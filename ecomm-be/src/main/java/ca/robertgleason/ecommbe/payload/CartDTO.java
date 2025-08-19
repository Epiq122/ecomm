package ca.robertgleason.ecommbe.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private final double totalPrice = 0.0;
    private Long cartId;
    private List<ProductDTO> products;
}
