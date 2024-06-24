package jwzp.cinema_city.controller;

import jwzp.cinema_city.models.Movie;
import jwzp.cinema_city.models.Screening;
import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.service.MovieService;
import jwzp.cinema_city.service.ScreeningService;
import jwzp.cinema_city.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jwzp.cinema_city.config.ClockConfig;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@RestController
public class PublicController {
    private static final Logger logger = LoggerFactory.getLogger(PublicController.class);

    @Autowired
    private Clock clock = Clock.systemDefaultZone();

    @Autowired
    private AuthService authService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ScreeningService screeningService;

    @PostMapping("/api/public/register")
    public ResponseEntity<String> registerUser(@RequestBody UserEntity user) {
        logger.info("Received request to register user: {}", user.getUsername());
        try {
            authService.registerUser(user);
            logger.info("User registered successfully: {}", user.getUsername());
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Failed to register user: {}", user.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
        }
    }

    @GetMapping("/api/user/movies-list")
    public ResponseEntity<List<Movie>> listMovies() {
        logger.info("Received request to list all movies");
        try {
            List<Movie> movies = movieService.findAllMovies();
            logger.info("Movies listed successfully, count: {}", movies.size());
            return new ResponseEntity<>(movies, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to list movies", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/api/user/movies-list-page")
    public ResponseEntity<List<Movie>> moviesPage(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "9") int size) {
        logger.info("Received request to list movies with pagination, page: {}, size: {}", page, size);
        try {
            Page<Movie> currentPage = movieService.getNextPage(page, size);
            logger.info("Movies page listed successfully, page: {}, size: {}, total elements: {}",
                    page, size, currentPage.getTotalElements());
            return new ResponseEntity<>(currentPage.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to list movies with pagination, page: {}, size: {}", page, size, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/api/public/screenings")
    public ResponseEntity<List<Screening>> showScreeningsByDate(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now(clock);
        }
        logger.info("Received request to list screenings for date: {}", date);
        try {
            List<Screening> screenings = screeningService.findScreeningsByDate(date);
            logger.info("Screenings listed successfully for date: {}, count: {}", date, screenings.size());
            return new ResponseEntity<>(screenings, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to list screenings for date: {}", date, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
