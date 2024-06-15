$(document).ready(function() {
    $('.form-signin').on('submit', function(event) {
        event.preventDefault();

        var username = $('#username').val();
        var password = $('#password').val();

        $.ajax({
            url: '/api/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ username: username, password: password }),
            success: function(response) {
                alert('Login successful');
                window.location.href = '/';
            },
            error: function(xhr, status, error) {
                $("#wrong-pass").removeClass("d-none");
            }
        });
    });
});