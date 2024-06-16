$(document).ready(function() {
    let currentReservation;
    let headers;
    function fetchSeats() {
        id = location.hash.substring(1);
        if (localStorage.getItem('jwtToken')) {
            headers = {Authorization: 'Bearer ' + localStorage.getItem('jwtToken')};
        }
        $.ajax({
            url: "/api/user/reserve",
            headers: headers,
            type: "GET",
            data: {id: id},
            success: function(reservation) {
                if(reservation) {
                    currentReservation = reservation;
                    $('#movie-title').text(reservation.screening.movie.title);
                    $('#screening-time').text(reservation.screening.screeningTime);
                    // $("input[name='screeningId']").attr('value', reservation.screening.id);
                    // $("input[name='movieTitle/']").attr('value', reservation.screening.movie.title);
                    // $("input[name='screeningTime']").attr('value', reservation.screening.screeningTime);
                    let seatContainer = $('#seats-selector');
                    seatContainer.empty();

                    reservation.screening.seats.forEach(function(seat, index) {
                        let seatLabel = $('<label></label>');
                        let seatInput = $('<input>', {
                            type: 'checkbox',
                            name: 'seats',
                            value: index,
                            checked: seat,
                            disabled: seat
                        });
                        let seatSpan = $('<span></span>', {
                            class: seat ? 'reserved' : 'available',
                            text: index + 1
                        });

                        seatLabel.append(seatInput).append(seatSpan);
                        seatContainer.append(seatLabel);
                    });
                }
                else {
                    $("#seats-selector").text('Unexpected response from server.');
                    $('.btn-primary').prop("disabled",true);
                }
            },
            error: function () {
                alert("Failed to fetch seats");
            }
        });
    }

    fetchSeats();

    $("form").submit(function(event) {
        event.preventDefault();

        let selectedSeats = [];
        $("#seats-selector input[name='seats']:checked").each(function() {
            if (!$(this).prop("disabled")) {
                selectedSeats.push(parseInt($(this).val()));
            }
        });

        currentReservation.seats = selectedSeats;
        $.ajax({
            url: "/api/user/reserve",
            headers: headers,
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(currentReservation),
            success: function(response) {
                console.log(JSON.stringify(currentReservation))
                console.log("Reservation sent");
            },
            error: function(xhr, status, error) {
                console.log("Reservation failed: " + error);
            }
        });
    });
});

/*
<h2 th:text="${screening.movie.title}">Movie Title</h2>
  <p th:text="${#temporals.format(screening.screeningTime, 'yyyy-MM-dd HH:mm')}">Screening Time</p>
  <form th:action="@{/reserve}" method="post">
    <input type="hidden" name="screeningId" th:value="${screening.id}"/>
    <input type="hidden" name="movieTitle" th:value="${screening.movie.title}"/>
    <input type="hidden" name="screeningTime" th:value="${#temporals.format(screening.screeningTime, 'yyyy-MM-dd HH:mm')}"/>
    <div th:each="seat, iterStat : ${screening.seats}">
      <label>
        <input type="checkbox" th:name="seats" th:value="${iterStat.index}" th:checked="${seat}" th:disabled="${seat}"/>
        <span th:classappend="${seat} ? 'reserved' : 'available'" th:text="${iterStat.index + 1}"></span>
      </label>
      <br th:if="${iterStat.index % 5 == 4}" />
    </div>
    <button type="submit" class="btn btn-primary">Reserve</button>
  </form>
 */