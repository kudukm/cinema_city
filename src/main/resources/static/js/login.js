$(document).ready(function() {
    $('.form-signin').on('submit', function(event) {
        event.preventDefault();

        let username = $('#username').val();
        let password = $('#password').val();

        $.ajax({
            url: '/api/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ username: username, password: password }),
            success: function(response) {
                if (response) {
                    $("#log-in-status").text('Log in successful');
                    localStorage.setItem('jwtToken', response);
                    window.location.href = '/';
                } else {
                    $("#log-in-status").text('Unexpected response from server');
                }
            },
            error: function() {
                $("#log-in-status").text('Invalid username or password');
            }
        });
    });
});