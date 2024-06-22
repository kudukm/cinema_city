package jwzp.cinema_city.controlerTests;

import jwzp.cinema_city.controller.PublicController;
import jwzp.cinema_city.models.Movie;
import jwzp.cinema_city.models.Screening;
import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.service.AuthService;
import jwzp.cinema_city.service.MovieService;
import jwzp.cinema_city.service.ScreeningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PublicControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private MovieService movieService;

    @Mock
    private ScreeningService screeningService;

    @InjectMocks
    private PublicController publicController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        UserEntity user = new UserEntity();

        ResponseEntity<String> response = publicController.registerUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());

        verify(authService, times(1)).registerUser(user);
    }

    @Test
    void testListMovies() {
        List<Movie> movies = new ArrayList<>();
        when(movieService.findAllMovies()).thenReturn(movies);

        ResponseEntity<List<Movie>> response = publicController.listMovies();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movies, response.getBody());
    }

    @Test
    void testMoviesPage() {
        int page = 1;
        int size = 10;
        Page<Movie> moviePage = mock(Page.class);
        List<Movie> content = new ArrayList<>();
        when(moviePage.getContent()).thenReturn(content);
        when(movieService.getNextPage(page, size)).thenReturn(moviePage);

        ResponseEntity<List<Movie>> response = publicController.moviesPage(page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(content, response.getBody());
        verify(movieService, times(1)).getNextPage(page, size);
    }

    @Test
    void testShowScreeningsByDate() {
        LocalDate date = LocalDate.now();
        List<Screening> screenings = new ArrayList<>();
        when(screeningService.findScreeningsByDate(date)).thenReturn(screenings);

        ResponseEntity<List<Screening>> response = publicController.showScreeningsByDate(date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(screenings, response.getBody());

        // Test default behavior when date is null
        response = publicController.showScreeningsByDate(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(screenings, response.getBody());

        verify(screeningService, times(2)).findScreeningsByDate(any());
    }
}
