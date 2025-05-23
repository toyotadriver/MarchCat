window.onload = function () {
    console.log('sosal');

    document.getElementById('div-form-login').addEventListener('submit', function (event) {
        event.preventDefault();

        const form = document.getElementById('div-form-login');

        var formData = new FormData(form);
        var name = formData.get('username');
        console.log(name);
        var password = formData.get('password');

        var request = new XMLHttpRequest();
        request.open('POST', 'login');
        request.setRequestHeader('username', name);
        request.setRequestHeader('password', password);
        request.send(formData);
    })
}

