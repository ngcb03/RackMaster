var credencial_global = null;
const eliminarLink = document.getElementById("eliminar_credencial");
const eliminarLinkOriginalHref = eliminarLink ? eliminarLink.href : null; // Almacenar el href original solo si existe

function openCredentialModal(credencial) {
    credencial.id_equipo = document.getElementById("elemento_id").value;
    credencial_global = credencial;

    // Asignar valores a los inputs
    document.getElementById("credencial_usuario").value = credencial.usuario || "";
    document.getElementById("credencial_puerto").value = credencial.puerto || "";
    document.getElementById("credencial_tipo_conexion").value = credencial.tipo_conexion || "";
    document.getElementById("credencial_privilegios").value = credencial.privilegios || "";
    document.getElementById("credencial_contrasenia").value = credencial.contrasenia || "";

    // Actualizar el enlace "Eliminar" solo si existe
    if (eliminarLink && eliminarLinkOriginalHref) {
        eliminarLink.href = eliminarLinkOriginalHref + credencial_global.credencial_id;
    }

    // Mostrar el modal y el fondo oscuro
    document.getElementById("credentialDetailModal").style.display = "block";
    document.getElementById("modalBackdrop").style.display = "block";
}

function closeCredentialModal() {
    // Restablecer el enlace "Eliminar" a su estado original
    if (eliminarLink && eliminarLinkOriginalHref) {
        eliminarLink.href = eliminarLinkOriginalHref;
    }

    // Ocultar la contraseña luego de cerrar el modal
    const passwordField = document.getElementById('credencial_contrasenia');
    const passwordToggleIcon = document.querySelector('.toggle-password');
    if (passwordField.type === "text") {
        passwordField.type = "password";
        passwordToggleIcon.textContent = "👁️";
    }

    // Ocultar el modal y el fondo oscuro
    document.getElementById("credentialDetailModal").style.display = "none";
    document.getElementById("modalBackdrop").style.display = "none";
}

function focusContrasenia() {
    toggleMessage(true);
}

function blurContrasenia() {
    toggleMessage(false);
}

function focusContraseniaConf() {
    toggleMessage(true);
}

function blurContraseniaConf() {
    toggleMessage(false);
}

function openRegisterCredentialModal() {
  document.getElementById("modal-title").innerHTML = "Registrar nueva credencial";
  document.getElementById("submitRegisterCredential").innerHTML = "Guardar credencial";

  document.getElementById("credencial_id").value = null;
  document.getElementById("id_equipo").value = document.getElementById("elemento_id").value;

  document.getElementById("usuario").value = "";
  document.getElementById("puerto").value = "";
  document.getElementById("tipo_conexion").value = "";
  document.getElementById("privilegios").value = "";
  document.getElementById("uso_destinado").value =  "";

  // Agregamos la restricción 'required' nuevamente de los campos contraseña y contraseña_conf
  document.getElementById("contrasenia").required = true;
  document.getElementById("contrasenia_conf").required = true;

  // Removemos los eventos onfocus y onblur
  document.getElementById("contrasenia").removeEventListener("focus", focusContrasenia);
  document.getElementById("contrasenia").removeEventListener("blur", blurContrasenia);
  document.getElementById("contrasenia_conf").removeEventListener("focus", focusContraseniaConf);
  document.getElementById("contrasenia_conf").removeEventListener("blur", blurContraseniaConf);

  const modal = document.getElementById("registerCredentialModal");
  modal.style.display = "block";
  document.getElementById("modalBackdrop").style.display = "block";
}


function openUpdateCredentialModal() {
  closeCredentialModal();

  // Cargar valores en el formulario de actualización
  document.getElementById("modal-title").innerHTML = "Actualizar credencial";
  document.getElementById("submitRegisterCredential").innerHTML = "Actualizar credencial";

  document.getElementById("id_equipo").value = credencial_global.id_equipo;
  document.getElementById("credencial_id").value = credencial_global.credencial_id;
  document.getElementById("usuario").value = credencial_global.usuario;
  document.getElementById("puerto").value = credencial_global.puerto;
  document.getElementById("tipo_conexion").value = credencial_global.tipo_conexion;
  document.getElementById("privilegios").value = credencial_global.privilegios;
  document.getElementById("uso_destinado").value =  credencial_global.uso_destinado;

   // Activar los eventos onfocus y onblur
   document.getElementById("contrasenia").addEventListener("focus", focusContrasenia);
   document.getElementById("contrasenia").addEventListener("blur", blurContrasenia);
   document.getElementById("contrasenia_conf").addEventListener("focus", focusContraseniaConf);
   document.getElementById("contrasenia_conf").addEventListener("blur", blurContraseniaConf);

   // Eliminar la restricción 'required' de los campos contraseña y contraseña_conf en el formulario de registro
   document.getElementById("contrasenia").removeAttribute("required");
   document.getElementById("contrasenia_conf").removeAttribute("required");

  const modal = document.getElementById("registerCredentialModal");
  modal.style.display = "block";
  document.getElementById("modalBackdrop").style.display = "block";
}

function closeModal() {
  document.getElementById("registerCredentialModal").style.display = "none";
  document.getElementById("modalBackdrop").style.display = "none";
}