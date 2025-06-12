function togglePassword() {
  const passwordField = document.getElementById('credencial_contrasenia');
  const passwordToggleIcon = document.querySelector('.toggle-password');

  if (passwordField.type === "password") {
    passwordField.type = "text";
    passwordToggleIcon.textContent = "🙈";
  } else {
    passwordField.type = "password";
    passwordToggleIcon.textContent = "👁️";
  }
}

function copyPassword() {
  var passwordField = document.getElementById("credencial_contrasenia");
  const passwordToggleIcon = document.querySelector('.toggle-password');

  // Verificar si la API de Clipboard está disponible
  if (navigator.clipboard && navigator.clipboard.writeText) {
    navigator.clipboard.writeText(passwordField.value).then(function () {
      // Mostrar mensaje de éxito al copiar
      var copySuccessMessage = document.getElementById("copy-success");
      copySuccessMessage.style.display = "block";

      // Ocultar el mensaje después de 3 segundos
      setTimeout(function () {
        copySuccessMessage.style.display = "none";
      }, 3000);
    }).catch(function (err) {
      console.error("Error al copiar la contraseña: ", err);
    });
  } else {
    // Usar el método antiguo si la API de Clipboard no está disponible
    passwordField.type = "text"; // Cambiar el tipo a texto temporalmente
    passwordField.select();
    passwordField.setSelectionRange(0, 99999); // Para dispositivos móviles

    try {
      var successful = document.execCommand('copy');
      if (successful) {
        // Mostrar mensaje de éxito al copiar
        var copySuccessMessage = document.getElementById("copy-success");
        copySuccessMessage.style.display = "block";

        // Cambiar de nuevo a tipo password
        passwordField.type = "password";
        passwordToggleIcon.textContent = "👁️";

        // Ocultar el mensaje después de 3 segundos
        setTimeout(function () {
          copySuccessMessage.style.display = "none";
        }, 3000);
      } else {
        console.error("Error al copiar la contraseña.");
      }
    } catch (err) {
      console.error("Error al copiar la contraseña: ", err);
    }
  }
}


// Función para validar todo el formulario al enviarlo
function validatePassword() {
    let allValid = true;

    let steps = document.querySelectorAll(".step");
    steps.forEach((step) => {
        if (!validateStep(step)) {
            allValid = false;
        }
    });

    if (!allValid) {
        alert("Por favor, complete todos los campos requeridos correctamente.");
        return false;
    }

    if (!validarContrasenas()) {
        return false;
    }

    return true;
}

function validarContrasenas() {
    let password = document.getElementById("contrasenia");
    let passwordConfirm = document.getElementById("contrasenia_conf");

    if (password.value !== passwordConfirm.value) {
        alert("Las contraseñas no coinciden. Por favor, inténtelo de nuevo.");
        password.classList.add("is-invalid");
        passwordConfirm.classList.add("is-invalid");
        return false;
    } else {
        password.classList.remove("is-invalid");
        passwordConfirm.classList.remove("is-invalid");
        return true;
    }
}

// Mostrar o ocultar mensaje de cambio de contraseña
function toggleMessage(isFocused) {
    var mess_passchange = document.getElementById("mess-passchange");
    var contrasenia = document.getElementById("contrasenia").value;
    var contrasenia_conf = document.getElementById("contrasenia_conf").value;

    if (isFocused || contrasenia || contrasenia_conf) {
        mess_passchange.style.display = "block";
    } else {
        mess_passchange.style.display = "none";
    }
}

// Inicializar el formulario para el primer paso
document.addEventListener("DOMContentLoaded", function () {
    let steps = document.querySelectorAll(".step");
    steps[0].classList.add("active-step"); // Mostrar el primer paso
    document.getElementById("prevBtn").style.display = "none"; // Ocultar el botón "Anterior" en el primer paso
});