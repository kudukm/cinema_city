package jwzp.cinema_city.controller;

import jwzp.cinema_city.models.Movie;
import jwzp.cinema_city.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MovieController {
    @Autowired
    private MovieService MovieService;

    @GetMapping("/addMovie")
    public String showRegistrationForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "addMovie";
    }

    @PostMapping("/addMovie")
    public String registerMovie(@ModelAttribute Movie movie) {
        MovieService.registerMovie(movie);
        return "redirect:/addMovie?success";
    }

    @GetMapping("/movies-list")
    public String listMovies(Model model) {
        List<Movie> movies = MovieService.findAllMovies();
        model.addAttribute("movies", movies);
        return "movies-list";
    }
}
