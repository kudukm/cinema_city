package jwzp.cinema_city.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = {"/", "/public/**", "/user/**", "/admin/**"})
    public String index() {
        return "index";
    }
}