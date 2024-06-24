package jwzp.cinema_city.controller;

import jwzp.cinema_city.models.Reservation;
import jwzp.cinema_city.models.Screening;
import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.service.ReservationService;
import jwzp.cinema_city.service.ScreeningService;
import jwzp.cinema_city.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ScreeningService screeningService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/me")
    public ResponseEntity<UserDetails> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();

        logger.info("Authenticated user retrieved: {}", currentUser.getUsername());
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/my-reservations")
    public ResponseEntity<List<Reservation>> myReservations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        logger.info("Retrieving reservations for user: {}", username);

        try {
            UserEntity user = userService.findByUsername(username);
            List<Reservation> reservations = userService.getMyReservations(user);
            logger.info("Reservations retrieved successfully for user: {}", username);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to retrieve reservations for user: {}", username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/reserve")
    public ResponseEntity<Reservation> showReservePage(@RequestParam(value = "id") String id) {
        logger.info("Showing reserve page for screening id: {}", id);

        try {
            Screening screening = screeningService.findScreeningById(id);
            Reservation reservation = new Reservation();
            reservation.setScreening(screening);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails currentUserDetails = (UserDetails) authentication.getPrincipal();
            UserEntity currentUserEntity = userService.findByUsername(currentUserDetails.getUsername());
            reservation.setUser(currentUserEntity);

            logger.info("Reserve page shown successfully for user: {}", currentUserEntity.getUsername());
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to show reserve page for screening id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/reserve")
    public ResponseEntity<Reservation> reserveSeats(@RequestBody Reservation reservation) {
        logger.info("Reserving seats for screening id: {}", reservation.getScreening().getId());

        try {
            // Additional logic for reserving seats can be added here
            logger.info("Seats reserved successfully for screening id: {}", reservation.getScreening().getId());
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to reserve seats for screening id: {}", reservation.getScreening().getId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/confirmReservation")
    public ResponseEntity<String> confirmReservation(@RequestBody Reservation reservation) {
        logger.info("Confirming reservation for user: {} and screening id: {}", reservation.getUser().getUsername(), reservation.getScreening().getId());

        try {
            reservationService.saveReservation(reservation);
            screeningService.updateSeatsForReservation(reservation);
            logger.info("Reservation confirmed successfully for user: {} and screening id: {}", reservation.getUser().getUsername(), reservation.getScreening().getId());
            return new ResponseEntity<>("Successful reservation", HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Failed to confirm reservation for user: {} and screening id: {}", reservation.getUser().getUsername(), reservation.getScreening().getId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to confirm reservation");
        }
    }
}
