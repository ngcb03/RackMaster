# RackMaster

**RackMaster** es una aplicación de software diseñada para gestionar la infraestructura de centros de datos (DataCenters). Permite documentar y organizar clústeres, servidores, máquinas virtuales, firewalls, routers y switches, e igualmente almacenar de manera segura las credenciales asociadas a estos recursos. Su objetivo es garantizar eficiencia operativa, seguridad y facilidad en la administración de recursos tecnológicos.

---

## Imágenes de Ejemplo

### 1. Interfaz Principal
![Interfaz Principal](https://github.com/user-attachments/assets/5129dab0-e1f0-41b9-975c-dc25bfacd1c1)

### 2. Gestión de Clústeres
![Gestión de Clústeres](https://github.com/user-attachments/assets/e008f3bc-3c35-4b06-810b-9be55246492f)

### 3. Información de Clúster
![Información de Clúster](https://github.com/user-attachments/assets/cd2ef9db-73b9-4a6f-a2b8-177e66a32951)

### 4. Gestión de Servidores
![Información de Clúster](https://github.com/user-attachments/assets/cd2ef9db-73b9-4a6f-a2b8-177e66a32951)

### 5. Información del Servidor (Pantalla 1)
![Información del Servidor 1](https://github.com/user-attachments/assets/c621811b-3b45-448a-a45f-15e5e5a02766)

### 6. Información del Servidor (Pantalla 2)
![Información del Servidor 2](https://github.com/user-attachments/assets/a6fc8681-4eec-4da1-9f1c-5484bd0ccb82)

### 7. Gestión de Máquinas Virtuales
![Gestión de Máquinas Virtuales](https://github.com/user-attachments/assets/83d5c8a7-08a7-413f-b6f1-a964ae0107b6)

### 8. Información de Máquina Virtual (Pantalla 1)
![Información de Máquina Virtual 1](https://github.com/user-attachments/assets/a07d4240-89c7-4c53-8968-b0ae42b2e8ea)

### 9. Información de Máquina Virtual (Pantalla 2)
![Información de Máquina Virtual 2](https://github.com/user-attachments/assets/ec2f6fbf-0ae1-402f-8802-79efd3214c4f)

### 10. Gestión de Cuentas
![Gestión de Cuentas](https://github.com/user-attachments/assets/4b7e783d-9263-4e63-bce4-e737f4b72fe6)

---

## Características

- **Gestión de servidores**: Administra y documenta los servidores físicos y virtuales de tu infraestructura.
- **Gestión de máquinas virtuales (VMs)**: Almacena información detallada sobre las máquinas virtuales, incluyendo su configuración y estado.
- **Gestión de clústeres**: Organiza y supervisa clústeres de servidores y máquinas virtuales.
- **Almacenamiento seguro de credenciales**: Guarda de forma segura las credenciales de acceso a los servidores y máquinas virtuales.
- **Interfaz intuitiva**: Una interfaz fácil de usar para facilitar la administración de todos los recursos en un solo lugar.

---

## Instalación

### Requisitos previos

1. Tener [Java 17+](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) instalado.
2. [Maven](https://maven.apache.org/install.html) para gestionar las dependencias y construir el proyecto.
3. Una base de datos configurada (como MySQL, PostgreSQL, etc.) para almacenar la información de la infraestructura.
4. En la raíz del proyecto se encuentra un archivo con la base de datos de prueba con el nombre 'rackmaster_data_postgresql.sql', que se usó en la explicación del video.

### Pasos de instalación

1. Clona este repositorio:

   ```bash
   git clone https://github.com/ngcb03/RackMaster.git
