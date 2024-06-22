package jwzp.cinema_city.repositoryTests;

import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
public class userRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void findByUsername_UserExists() {
        UserEntity user = new UserEntity();
        user.setUsername("Tom");
        user.setPassword("Tom123");
        user.setEmail("Tom@example.com");
        userRepository.insert(user);

        Optional<UserEntity> retrievedUser = userRepository.findByUsername("Tom");

        assertTrue(retrievedUser.isPresent());
        assertEquals(retrievedUser.get().getUsername(),"Tom");
    }

    @Test
    public void findByUsername_UserNotExists() {
        UserEntity user = new UserEntity();
        user.setUsername("Adam");
        user.setPassword("Adam123");
        user.setEmail("Adam@example.com");
        userRepository.insert(user);

        Optional<UserEntity> retrievedUser = userRepository.findByUsername("Tom");

        assertFalse(retrievedUser.isPresent());
    }
}
