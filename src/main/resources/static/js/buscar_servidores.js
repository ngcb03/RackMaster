// Función debounce para limitar la cantidad de búsquedas locales
    function debounce(func, delay) {
        let timeoutId;
        return function (...args) {
            if (timeoutId) {
                clearTimeout(timeoutId);
            }
            timeoutId = setTimeout(() => {
                func.apply(this, args);
            }, delay);
        };
    }

    // Función para buscar y mostrar la lista completa de servidores o filtrar según la consulta
    function buscarServidor(query) {
        const listaServidores = document.getElementById('lista-servidores');

        // Si no hay consulta, mostrar toda la lista
        const resultados = query.length < 1
            ? listaServidoresCompletos
            : listaServidoresCompletos.filter(servidor =>
                servidor.nombre.toLowerCase().includes(query.toLowerCase())
            );

        listaServidores.innerHTML = '';
        resultados.forEach(servidor => {
            const li = document.createElement('li');
            li.textContent = servidor.nombre;
            li.classList.add('list-group-item');
            li.setAttribute('data-id', servidor.servidor_id);

            li.onclick = () => {
                document.getElementById('hipervisor_asignar').value = servidor.nombre;
                document.getElementById('hipervisor_id').value = servidor.servidor_id;
                listaServidores.innerHTML = '';
            };

            listaServidores.appendChild(li);
        });
    }

    // Traer la lista de servidores desde el backend
    let listaServidoresCompletos = [];
    fetch('/rackmaster/servidores/a2/devolver-servidores')
        .then(response => response.json())
        .then(data => {
            listaServidoresCompletos = data;
        })
        .catch(error => console.error('Error al obtener los servidores:', error));

    // Mostrar toda la lista cuando se selecciona el input
    document.getElementById('hipervisor_asignar').addEventListener('focus', () => {
        buscarServidor('');  // Enviar una consulta vacía para mostrar toda la lista
    });

    // Filtrar la lista cuando el usuario escribe en el input
    document.getElementById('hipervisor_asignar').addEventListener('keyup', (event) => {
        buscarServidorConDebounce(event.target.value);  // Filtrar la lista al escribir
    });

    // Ocultar la lista de servidores cuando el campo pierde el enfoque
        document.getElementById('hipervisor_asignar').addEventListener('blur', () => {
            setTimeout(() => {
                document.getElementById('lista-servidores').innerHTML = '';
            }, 200);
        });

    // Aplica debounce a la función buscarServidor con un retraso de 300ms
    const buscarServidorConDebounce = debounce(buscarServidor, 300);