$(document).ready(function() {
    function fetchScreenings(date) {
        $.ajax({
            url: "/api/screenings",
            type: "GET",
            data: { date: date },
            success: function(screenings) {
                $("#selectedDate").text(date);
                if (screenings.length === 0) {
                    $("#noScreeningsMessage").removeClass("d-none");
                    $("#screeningsTable").addClass("d-none");
                } else {
                    $("#noScreeningsMessage").addClass("d-none");
                    $("#screeningsTable").removeClass("d-none");
                    $("#screeningsBody").empty();
                    screenings.forEach(function(screening) {
                        $("#screeningsBody").append(
                            `<tr>
                    <td>${screening.movie.title}</td>
                    <td>${screening.screeningTime}</td>
                    <td><a href="/reserve/${screening.id}" class="btn btn-primary">Reserve</a></td>
                  </tr>`
                        );
                    });
                }
            },
            error: function() {
                alert("Failed to fetch screenings");
            }
        });
    }


    // Initial fetch for today's date
    let today = new Date().toISOString().split('T')[0];
    $("#date").val(today);
    fetchScreenings(today);


    /*
    // Fetch screenings on date change
    $("#date").change(function() {
        let selectedDate = $(this).val();
        fetchScreenings(selectedDate);
    });*/

    // Fetch screenings on form submit
    $("form").submit(function(event) {
        event.preventDefault();
        let selectedDate = $("#date").val();
        fetchScreenings(selectedDate);
    });
});