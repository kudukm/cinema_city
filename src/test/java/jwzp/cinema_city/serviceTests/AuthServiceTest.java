package jwzp.cinema_city.serviceTests;

import jwzp.cinema_city.models.AuthRequest;
import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.repository.UserRepository;
import jwzp.cinema_city.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        UserEntity inputUser = new UserEntity();
        inputUser.setUsername("testUser");
        inputUser.setEmail("test@example.com");
        inputUser.setPassword("testPassword");

        UserEntity savedUser = new UserEntity();
        savedUser.setUsername("testUser");
        savedUser.setEmail("test@example.com");
        savedUser.setPassword("encodedPassword");

        when(passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);


        UserEntity result = authService.registerUser(inputUser);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());

        verify(passwordEncoder, times(1)).encode("testPassword");
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testAuthenticate_UserExists() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("testPassword");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("encodedPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        authService.authenticate(authRequest);

        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken("testUser", "testPassword")
        );

        UserEntity result = authService.authenticate(authRequest);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    public void testAuthenticate_UserNotFound() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("testPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            authService.authenticate(authRequest);
        });

        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken("testUser", "testPassword")
        );
    }
}
