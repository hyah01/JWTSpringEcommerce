package genspark.example.JWTSpringEcommerce.controller;


import genspark.example.JWTSpringEcommerce.entity.Product;
import genspark.example.JWTSpringEcommerce.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping("/")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> productList = productService.getAllProducts();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/names/sorted")
    public ResponseEntity<List<Product>> getSortedProductsByName() {
        List<Product> productList = productService.getBySortedName();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/sellers/sorted")
    public ResponseEntity<List<Product>> getSortedProductsBySeller() {
        List<Product> productList = productService.getBySortedSeller();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/name")
    public ResponseEntity<List<Product>> getProductByName(@RequestParam String name) {
        List<Product> productList = productService.getByName(name);
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/names")
    public ResponseEntity<List<Product>> getAllProductNames() {
        List<Product> productList = productService.getByNames();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/seller")
    public ResponseEntity<List<Product>> getProductBySeller(@RequestParam String seller) {
        List<Product> productList = productService.getBySellers(seller);
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/sellers")
    public ResponseEntity<List<Product>> getAllProductSellers() {
        List<Product> productList = productService.getBySellers();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable String category) {
        List<Product> productList = productService.getByCategory(category);
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = productService.getById(productId);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SELLER')")
    public ResponseEntity<List<Product>> addProduct( @RequestBody List<Product>  product) {
        List<Product>  addedProduct = productService.addProducts(product);
        return ResponseEntity.ok(addedProduct);
    }

    @PostMapping("/multiple")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SELLER')")
    public ResponseEntity<List<Product>> addProducts( @RequestBody List<Product> products, @RequestParam int num) {
        List<Product> addedProducts = productService.addMutlipleProduct(products, num);
        return ResponseEntity.ok(addedProducts);
    }

    @PutMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SELLER')")
    public ResponseEntity<List<Product>> updateProducts( @RequestBody List<Product> products) {
        List<Product> updatedProducts = productService.updateProduct(products);
        return ResponseEntity.ok(updatedProducts);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SELLER')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully");
    }
}