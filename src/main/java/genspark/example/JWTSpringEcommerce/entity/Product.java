package genspark.example.JWTSpringEcommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.context.SecurityContextHolder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private double price;
    private String seller;
    private String[] categories;

    public Product(String name, String description, double price, String[] categories) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.categories = categories;
    }

    @PrePersist
    protected void onCreate(){
        this.seller = SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
