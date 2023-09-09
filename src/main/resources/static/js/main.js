// $(document).ready(function() {
//     $("#locales").change(function () {
//         var selectedOption = $('#locales').val();
//         if (selectedOption != ''){
//             window.location.replace('?language=' + selectedOption);
//         }
//     });
// });

// main.js
import { subject, EditableElement } from './observer.js';

const hashtagsElement = new EditableElement("hashtags");
const descriptionElement = new EditableElement("description");

function enableEdit() {
    hashtagsElement.enableEditing();
    descriptionElement.enableEditing();
}

function saveChanges(photoId) {
    const hashtags = hashtagsElement.element.innerText;
    const description = descriptionElement.element.innerText;
    const csrfToken = document.querySelector('[data-csrf-token]').getAttribute('data-csrf-token');

    fetch('/profile/save-photo-changes/' + photoId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify({
            photoId: photoId,
            hashtags: hashtags,
            description: description
        })
    })
        .then(response => {
            // Handle the response as needed
        })
        .catch(error => {
            // Handle any errors
        });

    // Notify observers with updated data
    subject.notify({ hashtags, description });
}

document.addEventListener('click', function (event) {
    if (event.target && event.target.id === 'edit-button') {
        enableEdit();
    }
});

// Add observers if needed
// Example:
// subject.addObserver(someObserver);
