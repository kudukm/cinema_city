$(document).ready(function() {
    function checkIfLogged() {
        $.ajax({
            url: '/api/user/me',
            type: 'GET',
            success: function(user) {
                if(user) {
                    $('#logged-user').text(user.username);
                    $('#logged').removeClass('d-none');
                }
                else {
                    $('#non-logged').removeClass('d-none');
                }
            },
            error: function() {
                $("#logged-user").text('[Server rejected to answer]');
                $('#logged').removeClass('d-none');
            }
        });
    }
    checkIfLogged();

    $('.form-signin').on('submit', function(event) {
        event.preventDefault();

        let username = $('#username').val();
        let password = $('#password').val();

        $.ajax({
            url: '/api/public/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ username: username, password: password }),
            success: function(response) {
                if (response) {
                    $("#log-in-status").text('Log in successful').addClass('success-message').show();
                    window.location.href = '/';
                }
                else {
                    $("#log-in-status").text('Unexpected response from server').addClass('failure-message').show();
                }
            },
            error: function() {
                $("#log-in-status").text('Invalid username or password').addClass('failure-message').show();
            }
        });
    });
});

function logout() {
    $.ajax({
        url: '/api/public/logout',
        type: 'GET',
        success: function() {
            window.location.href = '/';
        },
        error: function() {
            alert('Logout unsuccessful.');
        }
    });
}