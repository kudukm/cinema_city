package jwzp.cinema_city.modelTests;

import jwzp.cinema_city.models.Movie;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovieTest {

    @Test
    public void TitleTest() {
        Movie movie = new Movie();

        movie.setTitle("testTitle");

        assertEquals(movie.getTitle(),"testTitle");
    }

    @Test
    public void PosterURLTest() {
        Movie movie = new Movie();

        movie.setPosterURL("https://testImage.pl/6925401_1.6.jpg");

        assertEquals(movie.getPosterURL(),"https://testImage.pl/6925401_1.6.jpg");
    }

    @Test
    public void DescriptionTest() {
        Movie movie = new Movie();

        movie.setDescription("testDescription");

        assertEquals(movie.getDescription(),"testDescription");
    }

    @Test
    public void DurationTest() {
        Movie movie = new Movie();

        movie.setDuration("2h 22m");

        assertEquals(movie.getDuration(),"2h 22m");
    }
}
