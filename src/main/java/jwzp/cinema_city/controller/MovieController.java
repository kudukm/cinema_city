package jwzp.cinema_city.controller;

import jakarta.servlet.http.HttpSession;
import jwzp.cinema_city.models.Movie;
import jwzp.cinema_city.models.Reservation;
import jwzp.cinema_city.models.Screening;
import jwzp.cinema_city.service.MovieService;
import jwzp.cinema_city.service.ReservationService;
import jwzp.cinema_city.service.ScreeningService;
import jwzp.cinema_city.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MovieController {
    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScreeningService screeningService;

    @Autowired
    private ReservationService reservationService;

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
        screening.setScreeningTime(LocalDateTime.now().plusDays(1)); // Set the screening time for example
        screeningService.addScreening(screening);
        return "addScreening";
    }

    @GetMapping("/screenings")
    public String showScreeningsByDate(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
        if (date == null) {
            date = LocalDate.now();
        }
        List<Screening> screenings = screeningService.findScreeningsByDate(date);
        model.addAttribute("screenings", screenings);
        model.addAttribute("selectedDate", date);
        return "screenings";
    }

    @GetMapping("/reserve/{id}")
    public String showReservePage(@PathVariable("id") String id, Model model) {
        Screening screening = screeningService.findScreeningById(id);
        model.addAttribute("screening", screening);
        return "reserve";
    }

    @PostMapping("/reserve")
    public String reserveSeats(@RequestParam String screeningId, @RequestParam String movieTitle,
                               @RequestParam String screeningTime, @RequestParam List<Integer> seats, Model model, HttpSession session) {
        session.setAttribute("screeningId", screeningId);
        session.setAttribute("movieTitle", movieTitle);
        session.setAttribute("screeningTime", screeningTime);
        session.setAttribute("selectedSeats", seats);
        return "payment";
    }

    @PostMapping("/confirmReservation")
    public String confirmReservation(@RequestParam String userId, HttpSession session) {
        Screening screening = screeningService.findScreeningById((String) session.getAttribute("screeningId"));
        List<Integer> selectedSeats = (List<Integer>) session.getAttribute("selectedSeats");

        if (selectedSeats == null || selectedSeats.isEmpty()) {
            return "redirect:/payment?error=noSeatsSelected";
        }

        Reservation reservation = new Reservation();
        reservation.setUser(userService.getUserByID(userId));
        reservation.setScreening(screening);
        reservation.setSeats(selectedSeats.stream().mapToInt(i->i).toArray());

        reservationService.saveReservation(reservation);
        screeningService.updateSeatsForReservation(reservation);

        return "reservation-success"; // Add a confirmation page or message
    }

}
