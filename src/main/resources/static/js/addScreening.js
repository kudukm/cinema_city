$(document).ready(function() {
    // Fetch movies from the backend and populate the select options
    $.ajax({
        url: '/api/admin/addScreening', // Replace with your backend endpoint
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

    // Handle form submission via AJAX
    $('#addScreeningForm').on('submit', function(event) {
            event.preventDefault(); // Prevent the default form submission

            // Collect form data
            var movieId = $('#movie').val();
            var screeningTime = $('#screeningTime').val(); // Make sure this is formatted correctly

            // Send AJAX POST request
            $.ajax({
                type: 'POST',
                url: '/admin/addScreening', // Replace with your backend endpoint
                data: {
                    movieId: movieId,
                    screeningTime: screeningTime
                },
                success: function(response) {
                    $('#success-message').show(); // Show success message
                    $('#addScreeningForm')[0].reset(); // Reset the form fields
                    $('#movie').val(''); // Reset select field
                },
                error: function(error) {
                    alert('Failed to add the screening. Please try again.');
                }
            });
        });
});
