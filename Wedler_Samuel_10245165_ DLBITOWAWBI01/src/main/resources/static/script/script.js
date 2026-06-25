/**
 * Accessible Serviceportal JavaScript
 * WCAG 2.1 AAA compliant event handling and accessibility features
 */

console.log("📋 Serviceportal Script loaded successfully");

// ===== State Management =====
let currentFontSize = localStorage.getItem("fontSize") || "medium";
let highContrast = localStorage.getItem("highContrast") === "true";

// Performance monitoring
const perfStart = performance.now();

// ===== Initialization =====
document.addEventListener("DOMContentLoaded", function() {
    console.log("🚀 DOM Content Loaded - Initializing accessibility features");
    
    try {
        // Apply saved settings
        applyFontSize(currentFontSize);
        applyContrast(highContrast);
        
        // Initialize modal events
        initializeModal();
        
        // Initialize keyboard shortcuts
        initializeKeyboardShortcuts();
        
        // Initialize checkbox listener for contrast on settings page
        initializeContrastCheckbox();
        
        // Sync settings with server
        syncSettingsWithServer();
        
        // Announce to screen readers that page is ready
        announceToScreenReader("Seite vollständig geladen");
        
        const perfEnd = performance.now();
        console.log(`✅ Initialization complete in ${(perfEnd - perfStart).toFixed(2)}ms`);
    } catch (error) {
        console.error("❌ Error during initialization:", error);
        announceToScreenReader("Fehler bei der Initialisierung der Seite");
    }
});

// ===== Font Size Management =====
function setFontSize(size) {
    try {
        if (!["small", "medium", "large"].includes(size)) {
            throw new Error(`Invalid font size: ${size}`);
        }
        
        currentFontSize = size;
        applyFontSize(size);
        localStorage.setItem("fontSize", size);
        
        // Visual feedback
        showStatusMessage(`Schriftgröße auf ${getSizeLabel(size)} gesetzt`);
        announceToScreenReader(`Schriftgröße geändert zu ${getSizeLabel(size)}`);
        
        console.log(`📝 Font size changed to: ${size}`);
    } catch (error) {
        console.error("Font size error:", error);
        showErrorMessage("Fehler beim Ändern der Schriftgröße");
        announceToScreenReader("Fehler beim Ändern der Schriftgröße");
    }
}

function applyFontSize(size) {
    try {
        document.body.className = document.body.className
            .replace(/\b(small|medium|large)\b/g, "")
            .trim();
        document.body.classList.add(size);
        
        // Update checkbox if on settings page
        updateContrastCheckbox();
    } catch (error) {
        console.error("Error applying font size:", error);
    }
}

function getSizeLabel(size) {
    const labels = {
        "small": "Klein (14px)",
        "medium": "Mittel (18px)",
        "large": "Groß (24px)"
    };
    return labels[size] || size;
}

// ===== Contrast Mode Management =====
function toggleContrast() {
    try {
        highContrast = !highContrast;
        applyContrast(highContrast);
        localStorage.setItem("highContrast", highContrast);
        
        // Visual feedback
        const status = highContrast ? "aktiviert" : "deaktiviert";
        showStatusMessage(`Kontrastmodus ${status}`);
        announceToScreenReader(`Hochkontrast-Modus ${status}`);
        
        // Update checkbox if on settings page
        updateContrastCheckbox();
        
        console.log(`🎨 Contrast mode: ${highContrast}`);
    } catch (error) {
        console.error("Contrast toggle error:", error);
        showErrorMessage("Fehler beim Umschalten des Kontrastmodus");
        announceToScreenReader("Fehler beim Umschalten des Kontrastmodus");
    }
}

function applyContrast(enabled) {
    try {
        if (enabled) {
            document.body.classList.add("high-contrast");
        } else {
            document.body.classList.remove("high-contrast");
        }
    } catch (error) {
        console.error("Error applying contrast:", error);
    }
}

function updateContrastCheckbox() {
    const checkbox = document.getElementById("contrastToggle");
    if (checkbox) {
        checkbox.checked = highContrast;
    }
}

function initializeContrastCheckbox() {
    try {
        const checkbox = document.getElementById("contrastToggle");
        if (checkbox) {
            checkbox.checked = highContrast;
            checkbox.addEventListener("change", function() {
                toggleContrast();
            });
            console.log("✅ Contrast checkbox initialized");
        }
    } catch (error) {
        console.error("Contrast checkbox initialization error:", error);
    }
}

// ===== Modal Management (Accessible) =====
function initializeModal() {
    try {
        const notificationBtn = document.getElementById("notificationBtn");
        const modal = document.getElementById("notificationModal");
        const modalClose = document.querySelector(".modal-close");
        
        if (notificationBtn) {
            notificationBtn.addEventListener("click", openModal);
        }
        
        if (modalClose) {
            modalClose.addEventListener("click", closeModal);
        }
        
        if (modal) {
            modal.addEventListener("click", function(e) {
                if (e.target === modal) {
                    closeModal();
                }
            });
        }
        
        // Keyboard: Escape to close modal
        document.addEventListener("keydown", function(e) {
            if (e.key === "Escape") {
                closeModal();
            }
        });
        
        console.log("✅ Modal initialized");
    } catch (error) {
        console.error("Error initializing modal:", error);
    }
}

function openModal() {
    try {
        const modal = document.getElementById("notificationModal");
        if (modal) {
            modal.classList.add("active");
            modal.setAttribute("aria-hidden", "false");
            document.body.style.overflow = "hidden"; // Prevent background scroll
            
            // Focus first focusable element in modal
            const firstFocusable = modal.querySelector("button, a, input");
            if (firstFocusable) {
                firstFocusable.focus();
            }
            
            announceToScreenReader("Benachrichtigungsmodal geöffnet");
        }
    } catch (error) {
        console.error("Error opening modal:", error);
        announceToScreenReader("Fehler beim Öffnen des Modals");
    }
}

function closeModal() {
    try {
        const modal = document.getElementById("notificationModal");
        if (modal) {
            modal.classList.remove("active");
            modal.setAttribute("aria-hidden", "true");
            document.body.style.overflow = "auto";
            
            // Return focus to trigger button
            const notificationBtn = document.getElementById("notificationBtn");
            if (notificationBtn) {
                notificationBtn.focus();
            }
            
            announceToScreenReader("Benachrichtigungsmodal geschlossen");
        }
    } catch (error) {
        console.error("Error closing modal:", error);
    }
}

// ===== Settings Persistence =====
function saveSettings() {
    try {
        const settings = {
            fontSize: currentFontSize,
            highContrast: highContrast
        };
        
        // Save to server
        fetch('/settings', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: JSON.stringify(settings)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("✅ Settings saved to server:", data);
            showStatusMessage("Einstellungen erfolgreich gespeichert");
            announceToScreenReader("Einstellungen erfolgreich gespeichert");
        })
        .catch(error => {
            console.error("❌ Error saving settings:", error);
            showErrorMessage("Fehler beim Speichern der Einstellungen. Versuchen Sie es später erneut.");
            announceToScreenReader("Fehler beim Speichern der Einstellungen");
        });
    } catch (error) {
        console.error("SaveSettings error:", error);
        showErrorMessage("Fehler beim Verarbeiten der Einstellungen");
    }
}

function syncSettingsWithServer() {
    try {
        // Fetch user settings from server (if available)
        fetch('/api/user/settings', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok && response.status !== 404) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            if (response.status === 404) {
                return null;
            }
            return response.json();
        })
        .then(data => {
            if (data && data.fontSize) {
                currentFontSize = data.fontSize;
                applyFontSize(currentFontSize);
                localStorage.setItem("fontSize", currentFontSize);
            }
            if (data && typeof data.highContrast !== 'undefined') {
                highContrast = data.highContrast;
                applyContrast(highContrast);
                localStorage.setItem("highContrast", highContrast);
            }
            console.log("✅ Settings synced from server");
        })
        .catch(error => {
            console.error("Settings sync error (non-critical):", error);
            // This is not critical, so we don't show an error to the user
        });
    } catch (error) {
        console.error("SyncSettingsWithServer error:", error);
    }
}

// ===== Keyboard Shortcuts =====
function initializeKeyboardShortcuts() {
    try {
        document.addEventListener("keydown", function(e) {
            // Alt + Z = Open Settings
            if (e.altKey && e.key === "z") {
                const settingsLink = document.querySelector('a[href="/settings"]');
                if (settingsLink) {
                    settingsLink.click();
                }
            }
            
            // Alt + H = Home
            if (e.altKey && e.key === "h") {
                const homeLink = document.querySelector('a[href="/"]');
                if (homeLink) {
                    homeLink.click();
                }
            }
        });
        console.log("✅ Keyboard shortcuts initialized");
    } catch (error) {
        console.error("Keyboard shortcuts error:", error);
    }
}

// ===== Accessibility Announcements =====
function announceToScreenReader(message) {
    try {
        // Create or reuse live region
        let liveRegion = document.getElementById("sr-announcements");
        if (!liveRegion) {
            liveRegion = document.createElement("div");
            liveRegion.id = "sr-announcements";
            liveRegion.className = "sr-only";
            liveRegion.setAttribute("aria-live", "polite");
            liveRegion.setAttribute("aria-atomic", "true");
            document.body.appendChild(liveRegion);
        }
        
        // Clear and set new message
        liveRegion.textContent = "";
        setTimeout(() => {
            liveRegion.textContent = message;
        }, 100);
    } catch (error) {
        console.error("Announcement error:", error);
    }
}

// ===== User Feedback Messages =====
function showStatusMessage(message) {
    try {
        const status = document.getElementById("fontSizeStatus") || document.getElementById("contrastDescription");
        if (status) {
            status.style.display = "block";
            const text = status.querySelector("span") || status;
            text.textContent = message;
            
            // Auto-hide after 3 seconds
            setTimeout(() => {
                status.style.display = "none";
            }, 3000);
        }
    } catch (error) {
        console.error("Status message error:", error);
    }
}

function showErrorMessage(message) {
    try {
        const container = document.querySelector("main") || document.body;
        const errorDiv = document.createElement("div");
        errorDiv.className = "error-message";
        errorDiv.setAttribute("role", "alert");
        errorDiv.innerHTML = `<i class="fas fa-exclamation-circle" aria-hidden="true"></i> ${message}`;
        
        container.insertBefore(errorDiv, container.firstChild);
        
        // Auto-remove after 5 seconds
        setTimeout(() => {
            errorDiv.remove();
        }, 5000);
    } catch (error) {
        console.error("Error message error:", error);
    }
}

// ===== Form Validation (Client-side) =====
document.addEventListener("DOMContentLoaded", function() {
    try {
        const forms = document.querySelectorAll("form");
        forms.forEach(form => {
            form.addEventListener("submit", function(e) {
                const isValid = validateForm(this);
                if (!isValid) {
                    e.preventDefault();
                    announceToScreenReader("Formularfehler: Bitte überprüfen Sie Ihre Eingaben");
                }
            });
        });
    } catch (error) {
        console.error("Form validation setup error:", error);
    }
});

function validateForm(form) {
    try {
        const inputs = form.querySelectorAll("input[required], textarea[required], select[required]");
        let isValid = true;
        
        inputs.forEach(input => {
            if (!input.value.trim()) {
                input.setAttribute("aria-invalid", "true");
                isValid = false;
            } else {
                input.setAttribute("aria-invalid", "false");
            }
        });
        
        return isValid;
    } catch (error) {
        console.error("Form validation error:", error);
        return true; // Allow submit if validation fails
    }
}

// ===== Cleanup on Page Unload =====
window.addEventListener("beforeunload", function() {
    try {
        // Ensure settings are saved before leaving
        if (currentFontSize !== localStorage.getItem("fontSize") || 
            highContrast.toString() !== localStorage.getItem("highContrast")) {
            console.log("Saving settings before unload");
            saveSettings();
        }
    } catch (error) {
        console.error("Cleanup error:", error);
    }
});