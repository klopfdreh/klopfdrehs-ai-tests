window.klopfdreh = {};

window.klopfdreh.prompt = function () {

    let recognitionToggle = document.getElementById('recognitionToggle');
    let promptInput = document.getElementById('promptInput');
    let promptForm = document.getElementById('promptForm');
    let promptChat = document.getElementById('promptChat');

    let recognition;
    try {
        recognition = new webkitSpeechRecognition();
    } catch (e) {
        recognition = Object;
    }
    recognition.continuous = true;
    recognition.interimResults = true;
    recognition.onresult = function (event) {
        let txtRec = '';
        for (let i = event.resultIndex; i < event.results.length; ++i) {
            txtRec += event.results[i][0].transcript;
        }
        promptInput.value = txtRec;
    };
    document.addEventListener('click', function (event) {
        if (event.target.id !== 'recognitionToggle') {
            return;
        }

        if (recognitionToggle.classList.contains('rt-disabled')) {

            recognitionToggle.classList.remove('rt-disabled');
            recognitionToggle.classList.add('rt-enabled');

            recognitionToggle.classList.remove('btn-primary');
            recognitionToggle.classList.add('btn-secondary');

            recognitionToggle.innerHTML = 'Stop prompting and send AI request'

            promptInput.focus();

        } else {
            if (promptForm.value !== '') {

                recognition.stop();

                const formBody = [];
                const encodedKey = encodeURIComponent("input");
                const encodedValue = encodeURIComponent(promptInput.value);
                formBody.push(encodedKey + "=" + encodedValue);
                let formBodyString = formBody.join("&");

                fetch(promptForm.action, {
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                    },
                    body: formBodyString
                }).then(response =>
                    response.json()
                ).then(result => {
                    recognitionToggle.classList.remove('rt-enabled');
                    recognitionToggle.classList.add('rt-disabled');

                    recognitionToggle.classList.remove('btn-secondary');
                    recognitionToggle.classList.add('btn-primary');

                    recognitionToggle.innerHTML = 'Start prompting'

                    let html = ''
                    html += `<div>
                            <div class="d-flex justify-content-between">
                                <p class="small mb-1"><u>You</u></p>
                                <p class="small mb-1 text-muted"></p>
                            </div>
                            <div class="d-flex flex-row justify-content-start">
                                <p class="small p-2 ms-3 mb-3 rounded-3" style="background-color: #f5f6f7;">${result.input}</p>
                            </div>
                            
                            <div class="d-flex justify-content-between">
                                <p class="small mb-1 text-muted"></p>
                                <p class="small mb-1"><u>AI</u></p>
                            </div>
                            <div class="d-flex flex-row justify-content-end mb-4 pt-1">
                                <div>
                                <p class="small p-2 me-3 mb-3 text-white rounded-3 bg-warning">${result.result}</p>
                                </div>
                            </div>
                        </div>`;
                    promptChat.innerHTML = html + promptChat.innerHTML;
                });
            }
        }
    });

    document.body.addEventListener('focusin', function (event) {

        if (event.target.id !== 'promptInput') {
            return;
        }

        if (!recognitionToggle.classList.contains('rt-enabled')) {
            return;
        }

        recognition.start();
    });

    document.body.addEventListener('click', function (event) {

        if (event.target.id !== 'clearChat') {
            return;
        }

        fetch(promptForm.action + "?clearKey=true", {
            method: "POST"
        }).then(response =>
            response.json()
        ).then(result => {
            promptChat.innerHTML = '';
        });
    });
};