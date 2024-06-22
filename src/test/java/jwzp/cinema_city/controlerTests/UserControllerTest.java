package jwzp.cinema_city.controlerTests;

import jwzp.cinema_city.controller.UserController;
import jwzp.cinema_city.models.Reservation;
import jwzp.cinema_city.models.Screening;
import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.service.ReservationService;
import jwzp.cinema_city.service.ScreeningService;
import jwzp.cinema_city.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ScreeningService screeningService;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticatedUser() {
        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        ResponseEntity<UserDetails> response = userController.authenticatedUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    void testMyReservations() {
        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(userService.findByUsername("testuser")).thenReturn(userEntity);

        List<Reservation> reservations = new ArrayList<>();
        when(userService.getMyReservations(userEntity)).thenReturn(reservations);

        ResponseEntity<List<Reservation>> response = userController.myReservations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservations, response.getBody());
    }

    @Test
    void testShowReservePage() {
        String screeningId = "screening123";
        Screening screening = new Screening();
        when(screeningService.findScreeningById(screeningId)).thenReturn(screening);

        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        when(userService.findByUsername("testuser")).thenReturn(userEntity);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        ResponseEntity<Reservation> response = userController.showReservePage(screeningId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(screening, response.getBody().getScreening());
        assertEquals(userEntity, response.getBody().getUser());
    }

    @Test
    void testReserveSeats() {
        Reservation reservation = new Reservation();

        ResponseEntity<Reservation> response = userController.reserveSeats(reservation);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservation, response.getBody());
    }

    @Test
    void testConfirmReservation() {
        Reservation reservation = new Reservation();

        ResponseEntity<String> response = userController.confirmReservation(reservation);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Successful reservation", response.getBody());

        verify(reservationService, times(1)).saveReservation(reservation);
        verify(screeningService, times(1)).updateSeatsForReservation(reservation);
    }
}
