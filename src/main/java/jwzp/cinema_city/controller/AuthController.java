package jwzp.cinema_city.controller;

import jwzp.cinema_city.models.AuthRequest;
import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.service.AuthService;
import jwzp.cinema_city.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final JwtService jwtService;

    private final AuthService authenticationService;

    public AuthController(JwtService jwtService, AuthService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/api/public/login")
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest loginUserDto) {
        UserEntity authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);
        ResponseCookie jwtCookie = ResponseCookie.from("jwt-token", jwtToken).path("/").httpOnly(true).build();

//        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body("OK");
    }

    @GetMapping("/api/public/logout")
    public ResponseEntity<String> logout() {
        ResponseCookie deleteCookie = ResponseCookie.from("jwt-token", null).path("/").maxAge(0).httpOnly(true).build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookie.toString()).body("OK");
    }
}
