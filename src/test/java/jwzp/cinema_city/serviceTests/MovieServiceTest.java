package jwzp.cinema_city.serviceTests;

import jwzp.cinema_city.models.Movie;
import jwzp.cinema_city.repository.MovieRepository;
import jwzp.cinema_city.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterMovie() {
        Movie movie = new Movie();
        when(movieRepository.save(movie)).thenReturn(movie);

        Movie result = movieService.registerMovie(movie);

        assertNotNull(result);
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    public void testFindAllMovies() {
        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        List<Movie> movies = Arrays.asList(movie1, movie2);
        when(movieRepository.findAll()).thenReturn(movies);

        List<Movie> result = movieService.findAllMovies();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    public void testFindMovieById_MovieExists() {
        Movie movie = new Movie();
        movie.setId("testId");
        when(movieRepository.findById("testId")).thenReturn(Optional.of(movie));

        Movie result = movieService.findMovieById("testId");

        assertNotNull(result);
        assertEquals("testId", result.getId());
    }

    @Test
    public void testFindMovieById_MovieNotFound() {
        when(movieRepository.findById("testId")).thenReturn(Optional.empty());

        Movie result = movieService.findMovieById("testId");

        assertNull(result);
    }

    @Test
    void testGetNextPage() {
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Movie movie = new Movie(); // Assuming Movie is your entity
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));

        when(movieRepository.findAll(pageable)).thenReturn(moviePage);

        Page<Movie> result = movieService.getNextPage(page, size);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(movieRepository, times(1)).findAll(pageable);
    }
}
