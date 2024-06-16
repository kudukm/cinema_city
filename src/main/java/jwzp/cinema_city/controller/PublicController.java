package jwzp.cinema_city.controller;


import jwzp.cinema_city.models.Movie;
import jwzp.cinema_city.models.Screening;
import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.service.MovieService;
import jwzp.cinema_city.service.ScreeningService;
import jwzp.cinema_city.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;

@RestController
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ScreeningService screeningService;


    /*@GetMapping("/")
    public ResponseEntity<String> home(){
        return new ResponseEntity<>("home", HttpStatus.OK);
    }*/

    @GetMapping("/api/public/register")
    public ResponseEntity<UserEntity> showRegistrationForm() {
        return new ResponseEntity<>(new UserEntity(), HttpStatus.OK);
    }

    @PostMapping("/api/public/register")
    public ResponseEntity<String> registerUser(@RequestBody UserEntity user) {
        userService.registerUser(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    /*@GetMapping("/api/login")
    public ResponseEntity<String> loginPage() {
        return new ResponseEntity<>("login", HttpStatus.OK);
    }*/

    @GetMapping("/api/user/movies-list")
    public ResponseEntity<List<Movie>> listMovies() {
        List<Movie> movies = movieService.findAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    //removed return of date attribute which should not be necessary
    @GetMapping("/api/public/screenings")
    public ResponseEntity<List<Screening>> showScreeningsByDate(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        List<Screening> screenings = screeningService.findScreeningsByDate(date);
        return new ResponseEntity<>(screenings, HttpStatus.OK);
    }
}
