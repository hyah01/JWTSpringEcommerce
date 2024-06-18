package genspark.example.JWTSpringEcommerce.service;

import genspark.example.JWTSpringEcommerce.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    List<Product> getBySellers(String seller);
    Product getById(long productID);
    List<Product> getByName(String name);
    List<Product> getByNames();
    List<Product> getBySellers();
    List<Product> getByCategory(String category);
    List<Product> getBySortedName();
    List<Product> getBySortedSeller();
    List<Product> addMutlipleProduct(List<Product> products, int amount);
    List<Product> addProducts(List<Product> products);
    Product addProduct(Product product);
    List<Product> updateProduct(List<Product> products);
    String deleteProduct(long productID);
}
