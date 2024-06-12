package jwzp.cinema_city.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Arrays;

@Document(collection = "screenings")
public class Screening {
    @Id
    private String id;

    @DBRef
    private Movie movie;

    private LocalDateTime screeningTime;

    private Boolean[] seats = new Boolean[40]; // Initialize with default values (null)

    public Screening() {
        // Initialize seats with all false (unreserved)
        Arrays.fill(seats, false);
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

    public void reserveSeat(int i){this.seats[i] = true;}

    public void emptySeat(int i){this.seats[i] = false;}

    public Boolean[] getSeats() {
        return seats;
    }
}

