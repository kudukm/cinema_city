package jwzp.cinema_city.controller;

import jwzp.cinema_city.models.AuthRequest;
import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.service.AuthService;
import jwzp.cinema_city.service.JwtService;
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

//        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(jwtToken);
    }
}
