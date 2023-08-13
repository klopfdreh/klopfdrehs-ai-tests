window.klopfdreh = {};

window.klopfdreh.settings = function () {

    let settingsForm = document.getElementById("settingsForm");

    document.addEventListener('click', function (event) {
        if (event.target.id !== 'settingsSubmit') {
            return;
        }
        settingsForm.submit();
    });
};