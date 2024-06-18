package genspark.example.JWTSpringEcommerce.repository;

import genspark.example.JWTSpringEcommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Retrieves list of products where name matches
    @Query("SELECT p from Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByName(String name);

    // Retrieves list of sellers where name matches
    @Query("SELECT p from Product p WHERE LOWER(p.seller) LIKE LOWER(CONCAT('%', :seller, '%'))")
    List<Product> findBySeller(String seller);

    // Custom query - Gets a list of all the distinct products
    @Query("SELECT DISTINCT product.name FROM Product product")
    List<String> findByNames();

    // Custom query - Gets a list of all the distinct sellers
    @Query("SELECT DISTINCT product.seller FROM Product product")
    List<String> findBySellers();

    //Sort by name / Sort by sellers
    @Query(value = "SELECT * from product ORDER BY name", nativeQuery = true)
    List<Product> findProductsByNameSort();

    @Query(value = "SELECT * from product ORDER BY seller", nativeQuery = true)
    List<Product> findProductsBySellerSort();
}
