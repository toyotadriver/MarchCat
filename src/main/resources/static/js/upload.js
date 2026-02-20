document.getElementById("uploadBtn").addEventListener("click", async () => {

    const fileInput = document.getElementById("fileInput");
    const file = fileInput.files[0];

    if (!file) {
        alert("Выберите файл");
        return;
    }

    document.getElementById("status").innerText = "Инициализация загрузки...";

    // 1️⃣ Получаем uploadUrl и uploadToken
    const initResponse = await fetch("/uploads/init", {
        method: "POST"
    });

    if (!initResponse.ok) {
        alert("Ошибка инициализации");
        return;
    }

    const initData = await initResponse.json();

    const uploadUrl ="picture/" + initData.uploadId + "?token=" + initData.uploadToken;

    document.getElementById("status").innerText = "Загрузка файла...";

    // 2️⃣ Отправляем файл в MCStorage
    const xhr = new XMLHttpRequest();

    xhr.open("POST", uploadUrl, true);

    xhr.upload.onprogress = function (event) {
        if (event.lengthComputable) {
            const percent = (event.loaded / event.total) * 100;
            document.getElementById("progressBar").value = percent;
        }
    };

    xhr.onload = function () {
        if (xhr.status === 200) {
            document.getElementById("status").innerText = "Файл успешно загружен!";
        } else {
            document.getElementById("status").innerText = "Ошибка загрузки";
        }
    };

    const formData = new FormData();
    formData.append("file", file);

    xhr.send(formData);
});
