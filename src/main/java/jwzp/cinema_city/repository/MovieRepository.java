package jwzp.cinema_city.repository;

import com.mongodb.lang.NonNull;
import jwzp.cinema_city.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
    @NonNull
    Page<Movie> findAll(@NonNull Pageable pageable);
}
