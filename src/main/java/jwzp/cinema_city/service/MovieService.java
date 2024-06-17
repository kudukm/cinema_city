package jwzp.cinema_city.service;


import jwzp.cinema_city.models.Movie;
import jwzp.cinema_city.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public Movie registerMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }

    public Page<Movie> getNextPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return movieRepository.findAll(pageable);
    }

    public Movie findMovieById(String id) {
        return movieRepository.findById(id).orElse(null);
    }
}
