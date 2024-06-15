package jwzp.cinema_city.controller;

import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Perform custom authentication logic (e.g., check password)
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, password);
        Authentication authentication = authenticationManager.authenticate(authToken);

        // If authentication successful, create JWT token or return success message
        // You can implement JWT creation logic or return a success message as per your requirement.

        // Example:
        // String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok("Login successful!"); // Replace with token generation logic if using JWT
    }
}
