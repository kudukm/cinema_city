package jwzp.cinema_city.controller;

import jwzp.cinema_city.models.AuthRequest;
import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.service.AuthService;
import jwzp.cinema_city.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final JwtService jwtService;

    private final AuthService authenticationService;

    public AuthController(JwtService jwtService, AuthService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/api/public/login")
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest loginUserDto) {
        logger.info("Received login request for user: {}", loginUserDto.getUsername());
        try {
            UserEntity authenticatedUser = authenticationService.authenticate(loginUserDto);
            logger.info("User authenticated successfully: {}", authenticatedUser.getUsername());

            String jwtToken = jwtService.generateToken(authenticatedUser);
            ResponseCookie jwtCookie = ResponseCookie.from("jwt-token", jwtToken).path("/").httpOnly(true).build();
            logger.debug("Generated JWT token for user: {}", authenticatedUser.getUsername());

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body("OK");
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", loginUserDto.getUsername(), e);
            return ResponseEntity.status(401).body("Authentication failed");
        }
    }

    @GetMapping("/api/public/logout")
    public ResponseEntity<String> logout() {
        logger.info("Received logout request");
        ResponseCookie deleteCookie = ResponseCookie.from("jwt-token", null).path("/").maxAge(0).httpOnly(true).build();
        logger.debug("Clearing JWT cookie");

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookie.toString()).body("OK");
    }
}
