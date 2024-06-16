$(document).ready(function() {
    function fetchMovies() {
        let headers;
        if (localStorage.getItem('jwtToken')) {
            headers = {Authorization: 'Bearer ' + localStorage.getItem('jwtToken')};
        }
        $.ajax({
            url: "/api/movies-list",
            headers: headers,
            type: "GET",
            success: function (movies) {
                if (movies.length >= 1) {
                    $("#movies-container").empty();
                    movies.forEach(function (movie) {
                        $("#movies-container").append(
                            `<div class="col-lg-4 col-md-6 mb-2">
                                <div class="position-relative mb-3">
                                    <img class="img-fluid rounded-3 mb-3" src="${movie.posterURL}" alt="..." />
                                    <h3 class="h3 fw-bolder text-decoration-none link-dark stretched-link">${movie.title}</h3>
                                </div>
                            </div>`
                        );
                    });
                }
            },
            error: function () {
                alert("Failed to fetch movies");
            }
        });
    }

    fetchMovies();
});