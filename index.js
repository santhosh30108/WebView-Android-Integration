let button = document.getElementById('webButton');
let text = document.getElementById('dataToSend')
let androidData = document.getElementById('androidData')
let str = ""
button.addEventListener("click",function(){
    str = text.value
    if ('webkit' in window){
        //webkit.messageHandlers.eventName.postMessage({parameters})
        webkit.messageHandlers.clickHandler.postMessage({str});
    }
    bridge.clickHandler(str);
})


function dataToWeb(data){
    androidData.innerText = data
}

// if (‘webkit’ in window)
//     webkit.messageHandlers.addBookmark.postMessage({‘id’: id});

// if (“webkit” in window)
//     webkit.messageHandlers.showNotebooks.postMessage({‘id’: id, ‘isBookmarked’: isBookmarked });
