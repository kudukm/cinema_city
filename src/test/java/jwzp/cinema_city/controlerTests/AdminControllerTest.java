package jwzp.cinema_city.controlerTests;

import jwzp.cinema_city.controller.AdminController;
import jwzp.cinema_city.models.Movie;
import jwzp.cinema_city.models.Screening;
import jwzp.cinema_city.service.MovieService;
import jwzp.cinema_city.service.ScreeningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    @Mock
    private MovieService movieService;

    @Mock
    private ScreeningService screeningService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowRegistrationForm() {
        ResponseEntity<Movie> response = adminController.showRegistrationForm();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddMovie_Success() {
        Movie movie = new Movie();

        ResponseEntity<String> response = adminController.addMovie(movie);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Movie created successfully", response.getBody());

        verify(movieService, times(1)).registerMovie(movie);
    }

    @Test
    void testAddMovie_Failure() {
        Movie movie = new Movie();
        doThrow(new RuntimeException("Failed to add movie")).when(movieService).registerMovie(movie);

        ResponseEntity<String> response = adminController.addMovie(movie);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to add the movie. Please try again.", response.getBody());

        verify(movieService, times(1)).registerMovie(movie);
    }

    @Test
    void testShowAddScreeningForm() {
        List<Movie> movies = new ArrayList<>();
        when(movieService.findAllMovies()).thenReturn(movies);

        ResponseEntity<List<Movie>> response = adminController.showAddScreeningForm();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movies, response.getBody());
    }

    @Test
    void testAddScreening_Success() {
        String movieId = "movie123";
        LocalDateTime screeningTime = LocalDateTime.now();
        Movie movie = new Movie();
        Screening screening = new Screening();
        screening.setMovie(movie);
        screening.setScreeningTime(screeningTime);

        when(movieService.findMovieById(movieId)).thenReturn(movie);

        ResponseEntity<String> response = adminController.addScreening(movieId, screeningTime);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Screening added successfully", response.getBody());
    }

    @Test
    void testAddScreening_Failure() {
        String movieId = "movie123";
        LocalDateTime screeningTime = LocalDateTime.now();

        doThrow(new RuntimeException("Failed to add screening")).when(screeningService).addScreening(any());

        ResponseEntity<String> response = adminController.addScreening(movieId, screeningTime);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to add the screening. Please try again.", response.getBody());
    }

    @Test
    void failTest(){
        assertEquals(1,2);
    }
}
