document.addEventListener("DOMContentLoaded", function() {
    var modal = document.getElementById("addUserModal");
    var btn = document.getElementById("addUserBtn");
    var span = document.getElementsByClassName("close")[0]; // Updated to get the first element only
    var iframe = document.getElementById("modalIframe");

    btn.onclick = function() {
        iframe.src = "http://localhost:9098/newGroupName"; // Set the URL of the content to be loaded
        modal.style.display = "block"; // Display the modal
    }

    span.onclick = function() {
        modal.style.display = "none"; // Close the modal when the close button is clicked
    }

    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none"; // Close the modal when clicked outside the modal
        }
    }
});
