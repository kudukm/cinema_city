package jwzp.cinema_city.service;


import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity registerUser(UserEntity user) {
        return userRepository.save(user);
    }
}
