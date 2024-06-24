package jwzp.cinema_city.controller;

import jwzp.cinema_city.models.Movie;
import jwzp.cinema_city.models.Screening;
import jwzp.cinema_city.service.MovieService;
import jwzp.cinema_city.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ScreeningService screeningService;

    @GetMapping("/api/admin/add-movie")
    public ResponseEntity<Movie> showRegistrationForm() {
        logger.info("Received request to show movie registration form");
        return new ResponseEntity<>(new Movie(), HttpStatus.OK);
    }

    @PostMapping("/api/admin/add-movie")
    public ResponseEntity<String> addMovie(@RequestBody Movie movie) {
        logger.info("Received request to add movie: {}", movie);
        try {
            movieService.registerMovie(movie);
            logger.info("Movie created successfully: {}", movie);
            return ResponseEntity.status(HttpStatus.CREATED).body("Movie created successfully");
        } catch (Exception e) {
            logger.error("Failed to add the movie: {}", movie, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add the movie. Please try again.");
        }
    }

    @GetMapping("/api/admin/add-screening")
    public ResponseEntity<List<Movie>> showAddScreeningForm() {
        logger.info("Received request to show add screening form");
        List<Movie> movies = movieService.findAllMovies();
        logger.debug("Found movies for screening form: {}", movies);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @PostMapping("/api/admin/add-screening")
    public ResponseEntity<String> addScreening(@RequestParam String movieId,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime screeningTime) {
        logger.info("Received request to add screening for movieId: {} at {}", movieId, screeningTime);
        try {
            Screening screening = new Screening();
            screening.setMovie(movieService.findMovieById(movieId));
            screening.setScreeningTime(screeningTime);

            screeningService.addScreening(screening);
            logger.info("Screening added successfully for movieId: {} at {}", movieId, screeningTime);
            return ResponseEntity.status(HttpStatus.CREATED).body("Screening added successfully");
        } catch (Exception e) {
            logger.error("Failed to add the screening for movieId: {} at {}", movieId, screeningTime, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add the screening. Please try again.");
        }
    }
}
