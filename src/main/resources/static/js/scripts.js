$(document).ready(function(){
    $('a').on('click', function(e) {
        e.preventDefault();
        let pageRef = $(this).attr('href');
        let pageTitle = $(this).text() + ' | CinemaCity'
            callPage(pageRef, pageTitle)
    });

    function callPage(pageRefInput, pageTitle) {
        $.ajax({
            url: pageRefInput,
            type: "GET",
            dataType: 'text',

            success: function(response) {
                history.pushState({},'', pageRefInput);
                document.title = pageTitle;
                $('article').html(response);
            },

            error: function(error) {
                console.log('The page wasn\'t loaded', error);
            }
        });
    }
});