console.log("script geladen");

function setFontSize(size) {
    document.body.className = size;
    localStorage.setItem("fontSize", size);
}

window.onload = () => {
    const size = localStorage.getItem("fontSize");
    if(size){
        document.body.className = size;
    }
}

function toggleContrast() {
    document.body.classList.toggle("high-contrast");
}