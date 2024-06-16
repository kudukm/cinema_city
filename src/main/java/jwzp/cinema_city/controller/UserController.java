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
import java.time.LocalDateTime;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

@RestController
//@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ScreeningService screeningService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/api/user/me")
    public ResponseEntity<UserDetails> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails currentUser = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }


    @GetMapping("/api/user/userPastReservations")
    public ResponseEntity<List<Reservation>> userPastReservations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        UserEntity user = userService.findByUsername(username);

        LocalDateTime time = LocalDateTime.now();

        List<Reservation> reservations = userService.getPastReservations(user,time);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/api/user/userFutureReservations")
    public ResponseEntity<List<Reservation>> userFutureReservations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        UserEntity user = userService.findByUsername(username);

        LocalDateTime time = LocalDateTime.now();

        List<Reservation> reservations = userService.getFutureReservations(user,time);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/api/user/reserve")
    public ResponseEntity<Reservation> showReservePage(@RequestParam(value = "id") String id) {
        Screening screening = screeningService.findScreeningById(id);
        Reservation reservation = new Reservation();
        reservation.setScreening(screening);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUserDetails = (UserDetails) authentication.getPrincipal();
        UserEntity currentUserEntity = userService.findByUsername(currentUserDetails.getUsername());
        reservation.setUser(currentUserEntity);

        return new ResponseEntity<>(reservation,HttpStatus.OK);
    }

    @PostMapping("/api/user/reserve")
    public ResponseEntity<Reservation> reserveSeats(@RequestBody Reservation reservation) {
        return new ResponseEntity<>(reservation,HttpStatus.OK);
    }

    @PostMapping("/api/user/confirmReservation")
    public ResponseEntity<String> confirmReservation(@RequestBody Reservation reservation) {
        reservationService.saveReservation(reservation);
        screeningService.updateSeatsForReservation(reservation);
        return new ResponseEntity<>("Successful reservation",HttpStatus.CREATED);
    }

}
