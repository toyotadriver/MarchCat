window.onload = function () {

    const renameBts = document.getElementsByClassName('renameBt')
    const linkBts = document.querySelectorAll('.linkBt')
    const deleteBts = document.querySelectorAll('.deleteBt')

    const notif = document.createElement('div');
    notif.classList.add('infobox');
    notif.textContent = 'Link copied to clipboard!';
    document.getElementById('wrapper').appendChild(notif);

    linkBts.forEach(linkBt => {
        linkBt.addEventListener('click', (clickEvent) => {

            console.log("Link copied!");

            navigator.clipboard.writeText(
                //TODO MUST CHANGE IT WHEN NEEDED
                //http://localhost:8080
                window.location.protocol + "//" +
                window.location.hostname + ":" +
                window.location.port + "/link/" +
                linkBt.getAttribute('data-link'));

            if (notif.style.display = 'none') {
                notif.style.display = 'flex';
                setTimeout(() => { notif.style.display = 'none' }, 2000);
            }
        })
    })

    deleteBts.forEach(deleteBt => {
        deleteBt.addEventListener('click', (clickEvent) => {
            console.log("DELETE CLICK!");
            
            console.log("Requested to delete file")

            action('DELETE', deleteBt.getAttribute('data-link'))
        })
    })

    function action(request, pictureLink) {
        console.log("DELETE!");
        
        var xhr = new XMLHttpRequest();
        xhr.open(request, 'account/' + pictureLink)
        xhr.setRequestHeader('pictureName', pictureLink)
        xhr.send();

        xhr.onload = function () {
            if (xhr.status == 200) {
                if (notif.style.display = 'none') {
                    notif.textContent = 'File requested to delete!';
                    notif.style.display = 'flex';
                    setTimeout(() => { notif.style.display = 'none' }, 2000);
                }

                location.reload();
            }
        }
    }


}


