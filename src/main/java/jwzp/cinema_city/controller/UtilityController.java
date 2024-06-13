package jwzp.cinema_city.controller;

import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UtilityController {
    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/panel")
    public String admin(){
        return "admin";
    }
}
