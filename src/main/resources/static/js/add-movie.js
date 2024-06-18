document.getElementById('addMovieForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const formData = {
        title: document.getElementById('title').value,
        posterURL: document.getElementById('posterURL').value,
        description: document.getElementById('description').value,
        duration: document.getElementById('duration').value
    };

    fetch('/api/admin/add-movie', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData),
    })
    .then(response => {
        if (response.ok) {
            document.getElementById('success-message').style.display = 'block';
            this.reset();
        } else {
            alert('Failed to add the movie. Please try again.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to add the movie. Please try again.');
    });
});
