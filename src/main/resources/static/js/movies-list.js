$(document).ready(function() {
    let pagesLoaded = 0;
    function fetchMoviesPage() {
        let headers;
        if (localStorage.getItem('jwtToken')) {
            headers = {Authorization: 'Bearer ' + localStorage.getItem('jwtToken')};
        }
        $.ajax({
            url: "/api/user/movies-list-page",
            headers: headers,
            type: "GET",
            data: {page: pagesLoaded},
            success: function (movies) {
                if (movies.length >= 1) {
                    // $("#movies-container").empty();
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
                else {
                    $("#movies-end").removeClass('d-none');
                }
            },
            error: function () {
                alert("Failed to fetch movies");
            }
        });
        pagesLoaded++;
    }

    fetchMoviesPage();

    function debounce(fn, delay) {
        let timeoutID;
        return function(...args) {
            if (timeoutID) clearTimeout(timeoutID);
            console.log(pagesLoaded);
            timeoutID = setTimeout(() => {
                fn(...args);
            }, delay);
        };
    }

    const debouncedFetchMoviesPage = debounce(fetchMoviesPage, 1000);

    $(window).scroll(function() {
        if ($(window).scrollTop() + $(window).height() > $(document).height() - 100) {
            debouncedFetchMoviesPage();
        }
    });
});