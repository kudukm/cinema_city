$(document).ready(function() {
    let pagesLoaded = 0;
    function fetchMoviesPage() {
        // console.log(pagesLoaded);
        if(pagesLoaded >= 0) {
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
                        $("#loading-message").text('Loading...')
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
                        $("#loading-message").text('All movies have been loaded.')
                        pagesLoaded = Number.MIN_SAFE_INTEGER; //prevents next invocations of the function after fetching all movies
                    }
                },
                error: function () {
                    $("#loading-message").text("Failed to fetch movies");
                }
            });
            pagesLoaded++;
        }
    }

    fetchMoviesPage();

    function throttle(fn, wait) {
        let lastTime = 0;
        return function(...args) {
            const now = new Date().getTime();
            if(now - lastTime >= wait) {
                lastTime = now;
                return fn(...args);
            }
        };
    }

    const checkScrollCondition = throttle(function() {
        if ($(window).scrollTop() + $(window).height() > $(document).height() - 100) {
            fetchMoviesPage();
        }
    }, 100); //checks scroll condition only once per 100ms

    $(window).scroll(function() {
        checkScrollCondition();
    });
});