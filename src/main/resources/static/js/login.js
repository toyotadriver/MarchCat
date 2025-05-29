window.onload = function () {
    console.log('sosal');

    document.getElementById('div-form-login').addEventListener('submit', function (event) {
        event.preventDefault();

        const form = document.getElementById('div-form-login');
        const messageSpan = document.getElementById('errorText');

        var formData = new FormData(form);
        var name = formData.get('username');
        console.log(name);
        var password = formData.get('password');

        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'login');
        xhr.setRequestHeader('username', name);
        xhr.setRequestHeader('password', password);
        xhr.send(formData);

        xhr.onload = function(){
            if(xhr.status == 200){
                var url = window.location.toString();
                window.location = url.replace('/login', '/main');
                
            } else{
                const responseMessage = xhr.responseText;
                messageSpan.textContent = responseMessage;
            }
        }
    })
}

