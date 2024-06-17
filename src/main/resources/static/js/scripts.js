$(document).ready(function() {
    function callPage(url) {
        let headers;
        if (localStorage.getItem('jwtToken')) {
            headers = {Authorization: 'Bearer ' + localStorage.getItem('jwtToken')};
        }
        $.ajax({
            url: url,
            headers: headers,
            dataType: 'html',
            success: function(data) {
                let content = $('#content')
                content.html(data);
                document.title = content.find('title').text() + ' | CinemaCity';
            },
            statusCode: {
                401: function() {
                    $('#content').html('<p>401 (Unauthorized) - Log in before opening this page.</p>');
                },
                403: function() {
                    $('#content').html("<p>403 (Forbidden) - You aren't allowed to see this page.</p>");
                },
                404: function() {
                    $('#content').html("<p>404 (Not found) - Page wasn't found.</p>");
                }
            },
            error: function() {
                $('#content').html('<p>Another page loading error.</p>');
            }
        });

        $('.nav-link').each(function() { //if link is a part of a menu activate it
            if($(this).attr('href') === location.pathname) {
                $(this).addClass('active');
                let potentialAccordion = $(this).parent().parent().parent();
                if(potentialAccordion.hasClass('accordion')) { //if the link is a part of an accordion open it
                    potentialAccordion.removeClass('d-none');
                    $('.accordion-arrow', potentialAccordion.parent()).text('▲');
                }
            }
        })
    }

    function getLocation() {
        const initialPath = location.pathname.substring(1);
        if(initialPath) {
            return '/html/' + initialPath + '.html' + location.search + location.hash;
        }
        else {
            return '/html/public/home.html';
        }
    }

    // Load the beginning content
    callPage(getLocation());


    // Handle link clicks
    $('a').on('click', function(e) {
        e.preventDefault();
        let pageRef = $(this).attr('href');
        let pageAddress = pageRef !== '/' ? pageRef.substring(1) + '.html' : 'public/home.html';
        callPage('/html/' + pageAddress);
        history.pushState(null, '', pageRef);
        if($(this).hasClass('nav-link')) {
            $('a.active').removeClass('active');
            $(this).addClass('active');
        }
    });

    // Handle browser navigation (back/forward)
    window.onpopstate = function() {
        let path = location.pathname.substring(1);
        let page = (path === '' ? 'public/home' : path) + '.html';
        callPage('/html/' + page);
    };

    $('div.nav-link').on('click', function() {
        let accordion = $('.accordion', $(this).parent());
        if(accordion.hasClass('d-none')) {
            accordion.removeClass('d-none');
            $('.accordion-arrow', $(this)).text('▲');
        }
        else {
            accordion.addClass('d-none');
            $('.accordion-arrow', $(this)).text('▼');
        }
    });
});