window.onload = function () {

    const renameBts = document.getElementsByClassName('renameBt')
    const linkBts = document.querySelectorAll('.linkBt')
    const deleteBts = document.getElementsByClassName('deleteBt')

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

            if(notif.style.display = 'none'){
                notif.style.display = 'flex';
                setTimeout(() => {notif.style.display = 'none'}, 2000);
            }
        })})
    }

    action = function(request, pictureName){
        var xhr = new XMLHttpRequest();
        xhr.open(request, 'account')
        xhr.setRequestHeader('pictureName', pictureName)
        xhr.send();
        
        xhr.onload = function(){
            if(xhr.status == 200){
                location.reload();
            }
        }
    }

