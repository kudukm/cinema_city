package jwzp.cinema_city.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping(value = {"/", "/public/**", "/user/**", "/admin/**"})
    public String index() {
        logger.info("Received request for index page");
        return "index";
    }
}
