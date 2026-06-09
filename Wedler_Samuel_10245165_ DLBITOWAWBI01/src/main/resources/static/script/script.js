console.log("script geladen");

let currentFontSize = localStorage.getItem("fontSize") || "medium";
let highContrast = localStorage.getItem("highContrast") === "true";

// Initial apply
window.onload = () => {
    document.body.className = currentFontSize;

    if (highContrast) {
        document.body.classList.add("high-contrast");
    }
};

function setFontSize(size) {
    currentFontSize = size;
    document.body.className = size;
    localStorage.setItem("fontSize", size);
}

function toggleContrast() {
    highContrast = !highContrast;

    document.body.classList.toggle("high-contrast");
    localStorage.setItem("highContrast", highContrast);
}

function openModal() {
    document.getElementById("notificationModal").style.display = "block";
}

function closeModal() {
    document.getElementById("notificationModal").style.display = "none";
}

function saveSettings() {
    fetch('/settings', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            fontSize: currentFontSize,
            highContrast: highContrast
        })
    });
}