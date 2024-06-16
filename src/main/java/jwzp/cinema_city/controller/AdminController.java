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

    @GetMapping("/admin/panel")
    public ResponseEntity<String> admin(){
        return new ResponseEntity<>("admin", HttpStatus.OK);
    }

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
    public ResponseEntity<Pair<List<Movie>,Screening>> showAddScreeningForm() {
        List<Movie> movies = movieService.findAllMovies();
        Pair<List<Movie>,Screening> response = Pair.of(movies, new Screening());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/admin/addScreening")
    public ResponseEntity<String> addScreening(@RequestBody Screening screening) {
        screeningService.addScreening(screening);
        return new  ResponseEntity<>("Created new Screening successfully", HttpStatus.CREATED);
    }

}
