package jwzp.cinema_city.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "screenings")
public class Screening {
    @Id
    private String id;

    @DBRef
    private Movie movie;

    private LocalDateTime screeningTime;

    public Screening() {}

    public Screening(Movie movie, LocalDateTime screeningTime) {
        this.movie = movie;
        this.screeningTime = screeningTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public LocalDateTime getScreeningTime() {
        return screeningTime;
    }

    public void setScreeningTime(LocalDateTime screeningTime) {
        this.screeningTime = screeningTime;
    }
}

