let button = document.getElementById('webButton');
let text = document.getElementById('dataToSend')
let androidData = document.getElementById('androidData')
let str = ""
button.addEventListener("click",function(){
    str = text.value
    bridge.clickHandler(str);
})


function dataToWeb(data){
    androidData.innerText = data
}