package jwzp.cinema_city.service;


import jwzp.cinema_city.models.Movie;
import jwzp.cinema_city.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
    @Autowired
    private MovieRepository MovieRepository;

    public Movie registerMovie(Movie movie) {
        return MovieRepository.save(movie);
    }
}
