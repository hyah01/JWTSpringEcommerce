package genspark.example.JWTSpringEcommerce.service;

import genspark.example.JWTSpringEcommerce.entity.Product;
import genspark.example.JWTSpringEcommerce.entity.UserInfo;
import genspark.example.JWTSpringEcommerce.repository.ProductRepository;
import genspark.example.JWTSpringEcommerce.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    public ProductRepository productDao;

    @Autowired
    UserInfoRepository userInfoRepository;

    public List<Product> getAllProducts() {
        return this.productDao.findAll();
    }

    @Override
    public List<Product> getBySellers(String seller) {
        return this.productDao.findBySeller(seller);
    }

    public Product getById(long productID) {

        Optional<Product> t = this.productDao.findById(productID);
        Product product = null;
        if (t.isPresent()) {
            product = t.get();
        }
        return product;
    }

    @Override
    public List<Product> getByName(String name) {
        return this.productDao.findByName(name);
    }

    @Override
    public List<Product> getByNames() {

        return this.productDao.findProductsByNameSort();
    }

    @Override
    public List<Product> getBySellers() {
        return this.productDao.findProductsBySellerSort();
    }

    @Override
    public List<Product> getByCategory(String category) {
        List<Product> allProducts = this.productDao.findAll();
        List<Product> matchCategory = new ArrayList<>();

        for( Product product: allProducts){
            for(String productCategory : product.getCategories()){
                if(productCategory.toLowerCase().contains(category.toLowerCase())){
                    matchCategory.add(product);
                    break;
                }
            }
        }
        return matchCategory;
    }

    @Override
    public List<Product> getBySortedName() {

        return this.productDao.findProductsByNameSort();
    }

    @Override
    public List<Product> getBySortedSeller() {
        return this.productDao.findProductsBySellerSort();
    }

    @Override
    public List<Product> addMutlipleProduct(List<Product> products, int amount) {
        List<Product> addedProducts = new ArrayList<>();
        // Ensure num is positive and greater than 0
        if (amount <= 0) {
            throw new IllegalArgumentException("Number of products to add must be greater than 0");
        }
        for (Product product : products) {
            for (int i = 0; i < amount; i++) {
                Product newProduct = new Product();
                newProduct.setName(product.getName());
                newProduct.setPrice(product.getPrice());
                newProduct.setDescription(product.getDescription());
                newProduct.setCategories(product.getCategories());
                addedProducts.add(productDao.save(newProduct));
            }
        }
        return addedProducts;
    }

    @Override
    public List<Product> addProducts(List<Product> productsToAdd) {
        List<Product> listOfProeducts = new ArrayList<>();
        for (Product productToAdd: productsToAdd){
            Product product = new Product(productToAdd.getName(),productToAdd.getDescription(),productToAdd.getPrice(),productToAdd.getCategories());
           listOfProeducts.add(product);
        }
        return this.productDao.saveAll(listOfProeducts);
    }
    @Override
    public Product addProduct(Product productToAdd) {
        Product product = new Product(productToAdd.getName(),productToAdd.getDescription(),productToAdd.getPrice(),productToAdd.getCategories());
        return this.productDao.save(product);
    }

    @Override
    public List<Product> updateProduct(List<Product> productsToUpdate) {
        List<Product> listOfProeducts = new ArrayList<>();
        for (Product productUpdate: productsToUpdate){
            Product product = productDao.findById(productUpdate.getId()).orElseThrow(() -> new RuntimeException("Product not found"));
            // authenticate to make sure only user of that post can edit it
            String curUserName = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<UserInfo> curUser = userInfoRepository.findByName(curUserName);
            if (curUser.isPresent()){
                UserInfo user = curUser.get();
                if (user.getRoles().equals("ADMIN") || product.getSeller().equals(curUserName)){
                    // Only allow user to edit title, content, tag and nothing else
                    product.setName(productUpdate.getName());
                    product.setDescription(productUpdate.getDescription());
                    product.setPrice(productUpdate.getPrice());
                    product.setCategories(productUpdate.getCategories());
                    listOfProeducts.add(product);
                } else {
                    throw new RuntimeException("No Permission");
                }
            } else {
                throw new RuntimeException("User Not Found");
            }
        }
        return this.productDao.saveAll(listOfProeducts);
    }

    @Override
    public String deleteProduct(long id) {
        Product product = this.productDao.findById(id).orElseThrow(() -> new RuntimeException(" Product not found"));
        // authenticate to make sure only user of that post can delete it
        String curUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserInfo> curUser = userInfoRepository.findByName(curUserName);
        if (curUser.isPresent()){
            UserInfo user = curUser.get();
            // Admin can also delete it too
            if (user.getRoles().equals("ADMIN") || product.getName().equals(curUserName)){
                this.productDao.deleteById(id);
                return "Product Deleted Successfully";
            } else {
                throw new RuntimeException("No Permission");
            }
        } else {
            throw new RuntimeException("User Not Found");
        }
    }


}
