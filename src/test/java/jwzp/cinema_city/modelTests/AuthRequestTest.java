package jwzp.cinema_city.modelTests;

import jwzp.cinema_city.models.AuthRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthRequestTest {

    @Test
    public void UsernameTest() {
        AuthRequest request = new AuthRequest();

        request.setUsername("testUser");

        assertEquals(request.getUsername(),"testUser");
    }

    @Test
    public void PasswordTest() {
        AuthRequest request = new AuthRequest();

        request.setPassword("testPassword");

        assertEquals(request.getPassword(),"testPassword");
    }
}
