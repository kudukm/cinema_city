$(document).ready(function() {
    let headers;
    if (localStorage.getItem('jwtToken')) {
        headers = {Authorization: 'Bearer ' + localStorage.getItem('jwtToken')};
    }

    $.ajax({
        url: '/api/admin/addScreening',
        headers: headers,
        method: 'GET',
        success: function(movies) {
            var select = $('#movie');
            movies.forEach(function(movie) {
                select.append('<option value="' + movie.id + '">' + movie.title + '</option>');
            });
        },
        error: function(error) {
            console.error('Error fetching movies:', error);
        }
    });

    $('#addScreeningForm').on('submit', function(event) {
            event.preventDefault();

            var movieId = $('#movie').val();
            var screeningTime = $('#screeningTime').val();

            $.ajax({
                headers: headers,
                type: 'POST',
                url: '/api/admin/addScreening',
                data: {
                    movieId: movieId,
                    screeningTime: screeningTime
                },
                success: function() {
                    $('#success-message').show();
                    $('#addScreeningForm')[0].reset();
                    $('#movie').val('');
                },
                error: function() {
                    alert('Failed to add the screening. Please try again.');
                }
            });
        });
});
