package jwzp.cinema_city.modelTests;


import jwzp.cinema_city.models.Reservation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationTest {

    @Test
    public void TitleTest() {
        Reservation reservation = new Reservation();

        reservation.setId("testId");

        assertEquals(reservation.getId(),"testId");
    }
}
