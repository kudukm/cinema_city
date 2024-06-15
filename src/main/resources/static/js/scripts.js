$(document).ready(function() {
    function callPage(url, pageTitle) {
        $.ajax({
            url: url,
            dataType: 'html',
            success: function(data) {
                document.title = pageTitle;
                $('#content').html(data);
            },
            error: function() {
                $('#content').html('<p>Error loading page.</p>');
            }
        });
    }

    function getLocation() {
        const initialPath = location.pathname.substring(1);
        if(initialPath) {
            return '/html/' + initialPath + '.html' + location.search;
        }
        else {
            return '/html/home.html';
        }
    }

    // Load the beginning content
    callPage(getLocation(), "CinemaCity");

    // Handle link clicks
    $('a').on('click', function(e) {
        e.preventDefault();
        let pageRef = $(this).attr('href');
        let pageAddress = pageRef !== '/' ? pageRef.substring(1) + '.html' : 'home.html';
        let pageTitle = $(this).text() + ' | CinemaCity'
        callPage('/html/' + pageAddress, pageTitle);
        history.pushState(null, '', pageRef);
    });

    // Handle browser navigation (back/forward)
    window.onpopstate = function() {
        var path = location.pathname.substring(1);
        var page = (path === '' ? 'home' : path) + '.html';
        callPage('/html/' + page);
    };
});