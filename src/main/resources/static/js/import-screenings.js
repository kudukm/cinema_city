let allMovies = [];
let headers;
if (localStorage.getItem('jwtToken')) {
    headers = {Authorization: 'Bearer ' + localStorage.getItem('jwtToken')};
}

$(document).ready(function() {
    let headers;
    if (localStorage.getItem('jwtToken')) {
        headers = {Authorization: 'Bearer ' + localStorage.getItem('jwtToken')};
    }
    $.ajax({
        url: '/api/admin/add-screening',
        headers: headers,
        method: 'GET',
        success: function(movies) {
            allMovies = movies;
        },
        error: function(error) {
            console.error('Error fetching movies:', error);
        }
    });

    document.getElementById('jsonFile').addEventListener("change", loadScreenings);
});

function loadScreenings() {
    const [file] = document.querySelector("input[type=file]").files;
    const reader = new FileReader();

    reader.addEventListener("load", () => {
            let screenings = JSON.parse(reader.result);
            presentScreenings(screenings);
        }, false,
    );

    if (file) {
        reader.readAsText(file);
    }
}

function presentScreenings(screenings) {
    if (screenings.length === 0) {
        $("#noScreeningsMessage").removeClass("d-none");
        $("#screeningsTable").addClass("d-none");
    } else {
        $("#noScreeningsMessage").addClass("d-none");
        $("#screeningsTable").removeClass("d-none");
        $("#screeningsBody").empty();
        screenings.forEach(function(screening) {
            movie = allMovies.find(movie => movie.id === screening.movieId);
            if(!movie) console.error('Movie with ID ' + screening.id + ' not found.');
            else {
                $("#screeningsBody").append(
                    `<tr>
                             <td><input type="checkbox"></td>
                             <td>${screening.movieId}</td>
                             <td>${movie.title}</td>
                             <td>${screening.screeningTime}</td>
                         </tr>`
                );
            }
        });
    }
}

function sortTable(columnIndex) {
    let table, rows, switching, i, x, y, shouldSwitch;
    table = document.getElementById('sortable-table');
    switching = true;
    // console.log("Table sorter run on column " + columnIndex);

    while (switching) {
        switching = false;
        rows = table.rows;

        for (i = 1; i < rows.length - 1; i++) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("td")[columnIndex];
            y = rows[i + 1].getElementsByTagName("td")[columnIndex];

            if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                shouldSwitch = true;
                break;
            }
        }

        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
        }
    }
}

$("form").submit(function(event) {
    event.preventDefault();
    let dateFrom = $("#date-from").val();
    let dateTo = $("#date-to").val();
    $('tbody tr').each(function() {
        let screeningTime =  $('td:last', $(this)).text();
        if(screeningTime < dateFrom || screeningTime > dateTo) {
            $(this).addClass('d-none');
        }
        else $(this).removeClass('d-none');
    });
});

function addScreenings() {
    let chosenScreenings = [];
    $('tbody tr').each(function() {
        if(!$(this).hasClass('d-none') && $('input:checked', $(this)).length === 1) {
            let movieId = $('td:nth-of-type(2)', $(this)).text();
            let screeningTime = $('td:nth-of-type(4)', $(this)).text();
            chosenScreenings.push({movieId: movieId, screeningTime: screeningTime});
        }
    });

    let wereErrors = false;
    chosenScreenings.forEach(function(screening) {
        $.ajax({
            headers: headers,
            type: 'POST',
            url: '/api/admin/add-screening',
            data: screening,
            success: function() {
                console.log(JSON.stringify(screening) + ' added successfully.');
            },
            error: function() {
                console.error('Failed to add the screening ' + JSON.stringify(screening) + '.');
                wereErrors = true;
            }
        });
    });
    if(!wereErrors) {
        $('#success-message').show();
        $('#failure-message').hide();
    }
    else {
        $('#success-message').hide();
        $('#failure-message').show();
    }
}