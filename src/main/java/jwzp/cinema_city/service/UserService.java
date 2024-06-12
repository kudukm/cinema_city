package jwzp.cinema_city.service;


import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity registerUser(UserEntity user) {
        return userRepository.save(user);
    }


    public UserEntity getUser(){
        List<UserEntity> reservations = userRepository.findAll();
        return reservations.isEmpty() ? null : reservations.get(0);
    }
}
