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


/**
 * アラートダイアログを表示する関数
 * @param {string} message - 表示するメッセージ
 */
function showAlert(message) {
    var dialog = document.getElementById('alertDialog');
    dialog.querySelector('p').innerHTML = message; // メッセージを設定
    dialog.style.display = 'flex'; // ダイアログを表示
}

/**
 * アラートダイアログを閉じる関数
 */
function closeAlert() {
    document.getElementById('alertDialog').style.display = 'none'; // ダイアログを非表示
}

/**
 * 確認ダイアログを表示する関数
 * @param {string} message - 表示するメッセージ
 * @param {function} callback - 結果を処理するコールバック関数
 */
function showConfirm(message, callback) {
    var dialog = document.getElementById('confirmDialog');
    dialog.querySelector('p').innerHTML = message; // メッセージを設定
    dialog.style.display = 'flex'; // ダイアログを表示
    window.confirmCallback = callback; // コールバック関数を設定
}

/**
 * 確認ダイアログを閉じる関数
 * @param {boolean} result - ユーザーの選択結果
 */
function closeConfirm(result) {
    document.getElementById('confirmDialog').style.display = 'none'; // ダイアログを非表示
    if (typeof window.confirmCallback === 'function') {
        window.confirmCallback(result); // コールバック関数を呼び出す
    }
}

/**
 * プロンプトダイアログを表示する関数
 * @param {string} message - 表示するメッセージ
 * @param {function} callback - 結果を処理するコールバック関数
 */
function showPrompt(message, callback) {
    var dialog = document.getElementById('promptDialog');
    dialog.querySelector('p').innerHTML = message; // メッセージを設定
    dialog.style.display = 'flex'; // ダイアログを表示
    window.promptCallback = callback; // コールバック関数を設定
}

/**
 * プロンプトダイアログを閉じる関数
 * @param {boolean} result - ユーザーの選択結果
 */
function closePrompt(result) {
    var dialog = document.getElementById('promptDialog');
    dialog.style.display = 'none'; // ダイアログを非表示
    if (typeof window.promptCallback === 'function') {
        window.promptCallback(result ? dialog.querySelector('input').value : null); // コールバック関数を呼び出す
    }
}
