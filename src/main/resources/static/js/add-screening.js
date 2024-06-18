$(document).ready(function() {
    $.ajax({
        url: '/api/admin/add-screening',
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
                type: 'POST',
                url: '/api/admin/add-screening',
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
