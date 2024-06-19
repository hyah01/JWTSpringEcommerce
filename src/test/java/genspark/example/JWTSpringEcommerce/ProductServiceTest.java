package genspark.example.JWTSpringEcommerce;

import genspark.example.JWTSpringEcommerce.entity.Product;
import genspark.example.JWTSpringEcommerce.entity.UserInfo;
import genspark.example.JWTSpringEcommerce.repository.ProductRepository;
import genspark.example.JWTSpringEcommerce.repository.UserInfoRepository;
import genspark.example.JWTSpringEcommerce.service.ProductServiceImpl;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.ActiveProfiles;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
public class ProductServiceTest {

	@Test
	public void test_add_valid_products() {
		ProductRepository productDao = mock(ProductRepository.class);
		UserInfoRepository userInfoRepository = mock(UserInfoRepository.class);
		ProductServiceImpl productService = new ProductServiceImpl();
		ReflectionTestUtils.setField(productService, "productDao", productDao);
		ReflectionTestUtils.setField(productService, "userInfoRepository", userInfoRepository);

		List<Product> productsToAdd = new ArrayList<>();
		Product product1 = new Product("Product1", "Description1", 10.0, new String[]{"Category1"});
		Product product2 = new Product("Product2", "Description2", 20.0, new String[]{"Category2"});
		productsToAdd.add(product1);
		productsToAdd.add(product2);

		when(productDao.saveAll(anyList())).thenReturn(productsToAdd);

		List<Product> addedProducts = productService.addProducts(productsToAdd);

		assertEquals(2, addedProducts.size());
		assertEquals("Product1", addedProducts.get(0).getName());
		assertEquals("Product2", addedProducts.get(1).getName());
	}

	@Test
	public void test_add_empty_product_list() {
		ProductRepository productDao = mock(ProductRepository.class);
		UserInfoRepository userInfoRepository = mock(UserInfoRepository.class);
		ProductServiceImpl productService = new ProductServiceImpl();
		ReflectionTestUtils.setField(productService, "productDao", productDao);
		ReflectionTestUtils.setField(productService, "userInfoRepository", userInfoRepository);

		List<Product> productsToAdd = new ArrayList<>();

		when(productDao.saveAll(anyList())).thenReturn(productsToAdd);

		List<Product> addedProducts = productService.addProducts(productsToAdd);

		assertTrue(addedProducts.isEmpty());
	}

	@Test
	public void test_delete_product_when_user_is_seller() {
		ProductRepository productDao = mock(ProductRepository.class);
		UserInfoRepository userInfoRepository = mock(UserInfoRepository.class);
		ProductServiceImpl productService = new ProductServiceImpl();
		ReflectionTestUtils.setField(productService, "productDao", productDao);
		ReflectionTestUtils.setField(productService, "userInfoRepository", userInfoRepository);

		Product product = new Product();
		product.setId(1L);
		product.setName("Test Product");
		product.setSeller("sellerUser");

		UserInfo user = new UserInfo();
		user.setName("sellerUser");
		user.setRoles("ROLE_SELLER");

		when(productDao.findById(1L)).thenReturn(Optional.of(product));
		when(userInfoRepository.findByName("sellerUser")).thenReturn(Optional.of(user));
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("sellerUser", null));

		String result = productService.deleteProduct(1L);

		verify(productDao).deleteById(1L);
		assertEquals("Product Deleted Successfully", result);
	}

	@Test(expected = RuntimeException.class)
	public void test_delete_product_that_does_not_exist() {
		ProductRepository productDao = mock(ProductRepository.class);
		UserInfoRepository userInfoRepository = mock(UserInfoRepository.class);
		ProductServiceImpl productService = new ProductServiceImpl();
		ReflectionTestUtils.setField(productService, "productDao", productDao);
		ReflectionTestUtils.setField(productService, "userInfoRepository", userInfoRepository);

		when(productDao.findById(1L)).thenReturn(Optional.empty());

		productService.deleteProduct(1L);
	}

	@Test
	public void test_update_product_successfully_when_user_is_seller() {
		ProductRepository productDao = mock(ProductRepository.class);
		UserInfoRepository userInfoRepository = mock(UserInfoRepository.class);
		ProductServiceImpl productService = new ProductServiceImpl();
		ReflectionTestUtils.setField(productService, "productDao", productDao);
		ReflectionTestUtils.setField(productService, "userInfoRepository", userInfoRepository);

		Product existingProduct = new Product("Old Name", "Old Description", 100.0, new String[]{"Category1"});
		existingProduct.setId(1L);
		existingProduct.setSeller("sellerUser");

		Product updatedProduct = new Product("New Name", "New Description", 150.0, new String[]{"Category2"});
		updatedProduct.setId(1L);

		UserInfo user = new UserInfo();
		user.setName("sellerUser");
		user.setRoles("ROLE_SELLER");

		when(productDao.findById(1L)).thenReturn(Optional.of(existingProduct));
		when(userInfoRepository.findByName("sellerUser")).thenReturn(Optional.of(user));
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("sellerUser", null));
		when(productDao.saveAll(anyList())).thenReturn(List.of(updatedProduct));

		List<Product> result = productService.updateProduct(List.of(updatedProduct));

		assertEquals(1, result.size());
		assertEquals("New Name", result.get(0).getName());
		assertEquals("New Description", result.get(0).getDescription());
		assertEquals(150.0, result.get(0).getPrice(), 0.01);
	}

	@Test(expected = RuntimeException.class)
	public void test_update_product_that_does_not_exist() {
		ProductRepository productDao = mock(ProductRepository.class);
		UserInfoRepository userInfoRepository = mock(UserInfoRepository.class);
		ProductServiceImpl productService = new ProductServiceImpl();
		ReflectionTestUtils.setField(productService, "productDao", productDao);
		ReflectionTestUtils.setField(productService, "userInfoRepository", userInfoRepository);

		Product nonExistentProduct = new Product("Non-existent Name", "Non-existent Description", 200.0, new String[]{"Category3"});
		nonExistentProduct.setId(99L);

		when(productDao.findById(99L)).thenReturn(Optional.empty());

		productService.updateProduct(List.of(nonExistentProduct));
	}


}

