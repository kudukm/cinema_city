$(document).ready(function() {
    function displayReservationSuccess() {
        if(location.hash === '#reservation-success') {
            $("#reservation-success").removeClass("d-none");
        }
    }

    function fetchReservations() {
        let headers;
        if (localStorage.getItem('jwtToken')) {
            headers = {Authorization: 'Bearer ' + localStorage.getItem('jwtToken')};
        }
        $.ajax({
            url: "/api/user/my-reservations",
            headers: headers,
            type: "GET",
            success: function (reservations) {
                if (reservations.length === 0) {
                    $("#no-reservations-message").removeClass("d-none");
                } else {
                    // console.log(JSON.stringify(reservations));
                    // $(".table").empty();
                    $("#reservations-container").removeClass("d-none");
                    reservations.forEach(function(reservation) {
                        $("#reservations-body").append(
                            `<tr>
                                <td>${reservation.screening.movie.title}</td>
                                <td>${reservation.screening.screeningTime}</td>
                                <td>${reservation.screening.movie.duration}</td>
                                <td>${reservation.seats}</td>
                            </tr>`
                        );
                    });
                }
            },
            error: function () {
                alert("Failed to fetch reservations");
            }
        });
    }

    displayReservationSuccess();
    fetchReservations();
});