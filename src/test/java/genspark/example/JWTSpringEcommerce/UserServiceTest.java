package genspark.example.JWTSpringEcommerce;

import genspark.example.JWTSpringEcommerce.entity.UserInfo;
import genspark.example.JWTSpringEcommerce.repository.UserInfoRepository;
import genspark.example.JWTSpringEcommerce.service.UserInfoService;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

import java.util.Optional;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    @Mock
    UserInfoRepository userDAO;
    @InjectMocks
    UserInfoService UserServiceImpl;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test_add_user_with_valid_information() {
        UserInfoRepository repository = mock(UserInfoRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        UserInfoService userInfoService = new UserInfoService();
        ReflectionTestUtils.setField(userInfoService, "repository", repository);
        ReflectionTestUtils.setField(userInfoService, "encoder", encoder);

        UserInfo userInfo = new UserInfo();
        userInfo.setName("testuser");
        userInfo.setEmail("testuser@example.com");
        userInfo.setPassword("password123");
        userInfo.setRoles("ROLE_USER");

        when(encoder.encode("password123")).thenReturn("encodedPassword123");

        String result = userInfoService.addUser(userInfo);

        verify(repository).save(userInfo);
        assertEquals("User Added Successfully", result);
    }


    @Test
    public void test_username_already_exists() {
        UserInfoRepository repository = mock(UserInfoRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        UserInfoService userInfoService = new UserInfoService();
        ReflectionTestUtils.setField(userInfoService, "repository", repository);
        ReflectionTestUtils.setField(userInfoService, "encoder", encoder);

        UserInfo existingUser = new UserInfo();
        existingUser.setName("existingUser");
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword("password123");
        existingUser.setRoles("USER");

        when(repository.findByName("existingUser")).thenReturn(Optional.of(existingUser));

        UserInfo newUser = new UserInfo();
        newUser.setName("existingUser");
        newUser.setEmail("new@example.com");
        newUser.setPassword("password123");
        newUser.setRoles("USER");

        String result = userInfoService.addUser(newUser);

        assertEquals("Username already exists", result);
    }
}
