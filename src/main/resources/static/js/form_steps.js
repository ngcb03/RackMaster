function nextStep(step) {
    let currentStep = document.querySelector(".step.active-step");
    let steps = document.querySelectorAll(".step");
    let currentIndex = Array.from(steps).indexOf(currentStep);
    let totalSteps = steps.length;

    // Validar el último paso antes de enviar el formulario
    if (currentIndex + step >= totalSteps) {
        if (!validateStep(currentStep)) {
            alert("Por favor, complete todos los campos requeridos correctamente.");
            return false;
        }

        // Validar contraseñas antes de enviar
        if (!validarContrasenas()) {
            return false;
        }

        // Enviar el formulario si todo es válido
        document.getElementById("multiStepForm").submit();
        return true;
    }

    // Validar antes de avanzar al siguiente paso
    if (step === 1 && validateStep(currentStep)) {
        changeStep(currentStep, currentIndex + 1);
    } else if (step === -1) {
        changeStep(currentStep, currentIndex - 1);
    }
}

// Función para cambiar entre los pasos
function changeStep(currentStep, newIndex) {
    let steps = document.querySelectorAll(".step");

    // Ocultar el paso actual
    currentStep.classList.remove("active-step");

    // Mostrar el nuevo paso
    steps[newIndex].classList.add("active-step");

    // Cambiar el texto del botón si es el último paso
    let nextBtn = document.getElementById("nextBtn");
    if (newIndex === steps.length - 1) {
        nextBtn.innerHTML = "Enviar";
    } else {
        nextBtn.innerHTML = "Siguiente";
    }

    // Mostrar u ocultar el botón "Anterior"
    document.getElementById("prevBtn").style.display = newIndex === 0 ? "none" : "inline";
}

// Función para validar campos de un paso específico
function validateStep(step) {
    let inputs = step.querySelectorAll("input[required], select[required]");
    let allFieldsValid = true;

    inputs.forEach((input) => {
        let minLength = input.getAttribute("minlength");
        let maxLength = input.getAttribute("maxlength");
        let min = input.getAttribute("min");
        let max = input.getAttribute("max");

        // Validar si el campo está vacío
        if (!input.value.trim()) {
            input.classList.add("is-invalid");
            allFieldsValid = false;
        } else {
            input.classList.remove("is-invalid");

            // Validar longitud mínima y máxima para campos de texto
            if (minLength && input.value.length < minLength) {
                input.classList.add("is-invalid");
                allFieldsValid = false;
                alert(`El campo ${input.name} debe tener al menos ${minLength} caracteres.`);
            } else if (maxLength && input.value.length > maxLength) {
                input.classList.add("is-invalid");
                allFieldsValid = false;
                alert(`El campo ${input.name} no debe exceder los ${maxLength} caracteres.`);
            }

            // Validar rango para campos numéricos
            if (input.type === "number" && (min || max)) {
                let value = parseFloat(input.value);
                if (min && value < min) {
                    input.classList.add("is-invalid");
                    allFieldsValid = false;
                    alert(`El valor de ${input.name} debe ser mayor o igual a ${min}.`);
                } else if (max && value > max) {
                    input.classList.add("is-invalid");
                    allFieldsValid = false;
                    alert(`El valor de ${input.name} debe ser menor o igual a ${max}.`);
                }
            }
        }
    });

    return allFieldsValid; // Retorna true si todos los campos están validados correctamente
}