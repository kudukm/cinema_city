$(document).ready(function() {
    $('.form-register').on('submit', function(event) {
        event.preventDefault();

        let username = $('#username').val();
        let password = $('#password').val();
        let email = $('#email').val();

        $.ajax({
            url: '/api/public/register',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ id: null, username: username, password: password, email: email, role: 'null'}),
            success: function(response) {
                if (response) {
                    $("#register-status").text('User registered successfully');
                }
                else {
                    $("#register-status").text('Unexpected response from server');
                }
            },
            error: function() {
                $("#register-status").text('Registration rejected by server');
            }
        });
    });
});