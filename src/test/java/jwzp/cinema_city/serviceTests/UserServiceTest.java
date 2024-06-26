package jwzp.cinema_city.serviceTests;

import jwzp.cinema_city.models.Reservation;
import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.repository.ReservationRepository;
import jwzp.cinema_city.repository.UserRepository;
import jwzp.cinema_city.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Clock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private UserService userService;

    private Clock fixedClock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testLoadUserByUsername_UserExists() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("testPassword");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        var userDetails = userService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("testPassword", userDetails.getPassword());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonExistentUser");
        });
    }

    @Test
    public void testGetUserByID_UserExists() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("testId");
        when(userRepository.findById("testId")).thenReturn(Optional.of(userEntity));

        UserEntity result = userService.getUserByID("testId");

        assertNotNull(result);
        assertEquals("testId", result.getId());
    }

    @Test
    public void testGetUserByID_UserNotFound() {
        when(userRepository.findById("testId")).thenReturn(Optional.empty());

        UserEntity result = userService.getUserByID("testId");

        assertNull(result);
    }

    @Test
    public void findByUsername_UserFound() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("testPassword");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        var userDetails = userService.findByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("testPassword", userDetails.getPassword());
    }

    @Test
    public void findByUsername_UserNotFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        var userDetails = userService.findByUsername("testUser");

        assertNull(userDetails);
    }

    @Test
    public void testGetMyReservations_UserHasReservations() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");

        Reservation reservation = new Reservation();
        reservation.setId("resId");
        reservation.setUser(userEntity);

        List<Reservation> reservations = Collections.singletonList(reservation);
        when(reservationRepository.findByUser(userEntity)).thenReturn(reservations);

        List<Reservation> result = userService.getMyReservations(userEntity);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("resId", result.get(0).getId());
    }

    @Test
    public void testGetMyReservations_UserHasNoReservations() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");

        when(reservationRepository.findByUser(userEntity)).thenReturn(Collections.emptyList());

        List<Reservation> result = userService.getMyReservations(userEntity);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


}
