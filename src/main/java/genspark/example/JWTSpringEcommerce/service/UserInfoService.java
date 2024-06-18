package genspark.example.JWTSpringEcommerce.service;

import genspark.example.JWTSpringEcommerce.entity.UserInfo;
import genspark.example.JWTSpringEcommerce.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserInfo> userDetail = repository.findByName(username);

        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

    public String addUser(UserInfo userInfo) {
        // Check if a user with the same name already exists
        Optional<UserInfo> existingUserByName = repository.findByName(userInfo.getName());
        if (existingUserByName.isPresent()) {
            return "Username already exists";
        }

        // Check if a user with the same email already exists
        Optional<UserInfo> existingUserByEmail = repository.findByEmail(userInfo.getEmail());
        if (existingUserByEmail.isPresent()) {
            return "Email already exists";
        }
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added Successfully";
    }


}