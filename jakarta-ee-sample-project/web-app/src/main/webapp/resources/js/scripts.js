function clearInput(clearButton) {
    var input = clearButton.previousElementSibling;
    if (input) {
        input.value = '';
        input.dispatchEvent(new Event('input'));  // For triggering any bound event listeners
        input.focus(); // Refocus the input
    }
}

function showClearButton(input) {
    var clearButton = input.parentNode.querySelector('.clearable__clear');
    clearButton.style.display = 'inline';
}

function hideClearButton(input) {
    var clearButton = input.parentNode.querySelector('.clearable__clear');
    setTimeout(function() {
        clearButton.style.display = 'none';
    }, 200);  // Allow time for click event to process before hiding
}
