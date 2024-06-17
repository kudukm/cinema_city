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

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AdminController {
    @Autowired
    private MovieService movieService;

    @Autowired
    private ScreeningService screeningService;

    @GetMapping("/api/admin/add-movie")
    public ResponseEntity<Movie> showRegistrationForm() {
        return new ResponseEntity<>(new Movie(), HttpStatus.OK);
    }

    @PostMapping("/api/admin/add-movie")
    public ResponseEntity<String> addMovie(@RequestBody Movie movie) {
        try {
            movieService.registerMovie(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body("Movie created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add the movie. Please try again.");
        }
    }

    @GetMapping("/api/admin/add-screening")
    public ResponseEntity<List<Movie>> showAddScreeningForm() {
        List<Movie> movies = movieService.findAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @PostMapping("/api/admin/add-screening")
    public ResponseEntity<String> addScreening( @RequestParam String movieId,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime screeningTime) {
        try {
            Screening screening = new Screening();
            screening.setMovie(movieService.findMovieById(movieId));
            screening.setScreeningTime(screeningTime);


            screeningService.addScreening(screening);

            return ResponseEntity.status(HttpStatus.CREATED).body("Screening added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Failed to add the screening. Please try again.");
        }
    }

}
