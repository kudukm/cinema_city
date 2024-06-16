document.getElementById('addMovieForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    // Serialize form data into JSON object
    const formData = {
        title: document.getElementById('title').value,
        posterURL: document.getElementById('posterURL').value,
        description: document.getElementById('description').value,
        duration: document.getElementById('duration').value
    };

    fetch('/admin/addMovie', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData),
    })
    .then(response => {
        if (response.ok) {
            document.getElementById('success-message').style.display = 'block';
            this.reset(); // Reset the form fields
        } else {
            alert('Failed to add the movie. Please try again.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to add the movie. Please try again.');
    });
});
