package jwzp.cinema_city.controller;

import jwzp.cinema_city.models.Movie;
import jwzp.cinema_city.models.Screening;
import jwzp.cinema_city.service.MovieService;
import jwzp.cinema_city.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MovieController {
    @Autowired
    private MovieService movieService;

    @Autowired
    private ScreeningService screeningService;

    @GetMapping("/addMovie")
    public String showRegistrationForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "addMovie";
    }

    @PostMapping("/addMovie")
    public String registerMovie(@ModelAttribute Movie movie) {
        movieService.registerMovie(movie);
        return "redirect:/addMovie?success";
    }

    @GetMapping("/movies-list")
    public String listMovies(Model model) {
        List<Movie> movies = movieService.findAllMovies();
        model.addAttribute("movies", movies);
        return "movies-list";
    }

    @GetMapping("/addScreening")
    public String showAddScreeningForm(Model model) {
        List<Movie> movies = movieService.findAllMovies();
        model.addAttribute("movies", movies);
        model.addAttribute("screening", new Screening());
        return "addScreening";
    }

    @PostMapping("/addScreening")
    public String addScreening(@ModelAttribute Screening screening) {
        screeningService.registerScreening(screening);
        return "redirect:/addScreening";
    }
}
