package jwzp.cinema_city.controller;

import jwzp.cinema_city.models.Movie;
import jwzp.cinema_city.models.Screening;
import jwzp.cinema_city.service.MovieService;
import jwzp.cinema_city.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

@RestController
public class AdminController {
    @Autowired
    private MovieService movieService;

    @Autowired
    private ScreeningService screeningService;

    @GetMapping("/api/admin/addMovie")
    public ResponseEntity<Movie> showRegistrationForm() {
        return new ResponseEntity<>(new Movie(), HttpStatus.OK);
    }

    @PostMapping("/admin/addMovie")
    public ResponseEntity<String> addMovie(@RequestBody Movie movie) {
        try {
            movieService.registerMovie(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body("Movie created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add the movie. Please try again.");
        }
    }

    @GetMapping("/api/admin/addScreening")
    public ResponseEntity<List<Movie>> showAddScreeningForm() {
        List<Movie> movies = movieService.findAllMovies();;
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @PostMapping("/admin/addScreening")
    public ResponseEntity<String> addScreening(@RequestBody Screening screening) {
        try {
            screeningService.addScreening(screening);
            return ResponseEntity.status(HttpStatus.CREATED).body("Screening added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add the screening. Please try again.");
        }
    }

}
