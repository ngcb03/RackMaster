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

    // Función para buscar y mostrar la lista completa de clusters o filtrar según la consulta
    function buscarCluster(query) {
        const listaClusters = document.getElementById('lista-clusters');

        // Si no hay consulta, mostrar toda la lista
        const resultados = query.length < 1
            ? listaClustersCompletos
            : listaClustersCompletos.filter(cluster =>
                cluster.nombre.toLowerCase().includes(query.toLowerCase())
            );

        listaClusters.innerHTML = '';
        resultados.forEach(cluster => {
            const li = document.createElement('li');
            li.textContent = cluster.nombre;
            li.classList.add('list-group-item');
            li.setAttribute('data-id', cluster.cluster_id);

            li.onclick = () => {
                document.getElementById('cluster_asignar').value = cluster.nombre;
                document.getElementById('cluster_id').value = cluster.cluster_id;
                listaClusters.innerHTML = '';
            };

            listaClusters.appendChild(li);
        });
    }

    // Traer la lista de servidores desde el backend
    let listaClustersCompletos = [];
    fetch('/rackmaster/clusters/a2/devolver-clusters')
        .then(response => response.json())
        .then(data => {
            listaClustersCompletos = data;
        })
        .catch(error => console.error('Error al obtener los clusters:', error));

    // Mostrar toda la lista cuando se selecciona el input
    document.getElementById('cluster_asignar').addEventListener('focus', () => {
        buscarCluster('');  // Enviar una consulta vacía para mostrar toda la lista
    });

    // Filtrar la lista cuando el usuario escribe en el input
    document.getElementById('cluster_asignar').addEventListener('keyup', (event) => {
        buscarClusterConDebounce(event.target.value);  // Filtrar la lista al escribir
    });

    // Ocultar la lista de clusters cuando el campo pierde el enfoque
        document.getElementById('cluster_asignar').addEventListener('blur', () => {
            setTimeout(() => {
                document.getElementById('lista-clusters').innerHTML = '';
            }, 200);
        });

    // Aplica debounce a la función buscarCluster con un retraso de 300ms
    const buscarClusterConDebounce = debounce(buscarCluster, 300);