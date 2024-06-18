package genspark.example.JWTSpringEcommerce.repository;

import genspark.example.JWTSpringEcommerce.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByName(String username);

    Optional<UserInfo> findByEmail(String email);
}

