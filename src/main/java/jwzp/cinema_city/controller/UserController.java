package jwzp.cinema_city.controller;

import jwzp.cinema_city.models.Reservation;
import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserEntity());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.registerUser(user);
        return "redirect:/register?success";
    }

    @GetMapping("/userPastReservations")
    public String userPastReservations(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        UserEntity user = userService.findByUsername(username);

        LocalDateTime time = LocalDateTime.now();

        List<Reservation> reservations = userService.getPastReservations(user,time);
        model.addAttribute("reservations", reservations);
        return "userPastReservations";
    }

    @GetMapping("/userFutureReservations")
    public String userFutureReservations(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        UserEntity user = userService.findByUsername(username);

        LocalDateTime time = LocalDateTime.now();

        List<Reservation> reservations = userService.getFutureReservations(user,time);
        model.addAttribute("reservations", reservations);
        return "userFutureReservations";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "customLogin";
    }

}
