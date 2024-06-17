$(document).ready(function() {
    function checkIfLogged() {
        if (!localStorage.getItem('jwtToken')) {
            $('#non-logged').removeClass('d-none');
        }
        else {
            headers = {Authorization: 'Bearer ' + localStorage.getItem('jwtToken')};
            $.ajax({
                url: '/api/user/me',
                headers: headers,
                type: 'GET',
                success: function(user) {
                    if (user) {
                        $("#logged-user").text(user.username);
                    }
                    else {
                        $("#logged-user").text('[Unexpected response from server]');
                    }
                },
                error: function() {
                    $("#logged-user").text('[Server rejected to answer]');
                }
            });
            $('#logged').removeClass('d-none');
        }
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
                    $("#log-in-status").text('Log in successful');
                    localStorage.setItem('jwtToken', response);
                    window.location.href = '/';
                }
                else {
                    $("#log-in-status").text('Unexpected response from server');
                }
            },
            error: function() {
                $("#log-in-status").text('Invalid username or password');
            }
        });
    });
});

function logout() {
    localStorage.removeItem('jwtToken')
    window.location.href = '/';
}