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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ScreeningService screeningService;

    @Autowired
    private ReservationService reservationService;


    @GetMapping("/userPastReservations")
    public ResponseEntity<List<Reservation>> userPastReservations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        UserEntity user = userService.findByUsername(username);

        LocalDateTime time = LocalDateTime.now();

        List<Reservation> reservations = userService.getPastReservations(user,time);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/userFutureReservations")
    public ResponseEntity<List<Reservation>> userFutureReservations(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        UserEntity user = userService.findByUsername(username);

        LocalDateTime time = LocalDateTime.now();

        List<Reservation> reservations = userService.getFutureReservations(user,time);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/reserve")
    public ResponseEntity<Pair<Screening,Reservation>> showReservePage(@RequestBody String id) {
        Screening screening = screeningService.findScreeningById(id);
        Pair<Screening,Reservation> response = Pair.of(screening,new Reservation());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/reserve")
    public ResponseEntity<Reservation> reserveSeats(@RequestBody Reservation reservation) {
        return new ResponseEntity<>(reservation,HttpStatus.OK);
    }

    @PostMapping("/confirmReservation")
    public ResponseEntity<String> confirmReservation(@RequestBody Reservation reservation) {
        reservationService.saveReservation(reservation);
        screeningService.updateSeatsForReservation(reservation);
        return new ResponseEntity<>("Succesfull reservation",HttpStatus.CREATED);
    }

}
