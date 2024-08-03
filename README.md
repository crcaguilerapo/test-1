# Test-1

## Requerimientos

- **JDK 21**: Asegúrate de tener instalado Java Development Kit (JDK) versión 21.
- **Gradle 8.8**: El proyecto utiliza Gradle versión 8.8, que debe ejecutarse sobre JDK 21.
- **Docker**: Docker debe estar instalado para ejecutar la aplicación en un contenedor.

## Instrucciones

Para ejecutar la aplicación dockerizada, sigue estos pasos:

1. Ubícate en la raíz del proyecto.
2. Ejecuta el siguiente comando para iniciar los servicios en contenedores:
   ```sh
   docker-compose up -d
Para ejecutar la base de datos en Docker y la aplicación en local, sigue estos pasos:

1. Ubícate en la raíz del proyecto.
2. Ejecuta el siguiente comando para iniciar la base de datos dockerizada:
   ```sh
   docker-compose -f docker-compose.local.yml up
3. Inicia la aplicación con el profile local:
   ```sh
   --spring.profiles.active=local
## Códigos de Error

La aplicación maneja los siguientes códigos de error:

- **FRANCHISE_ALREADY_EXISTS**: `"The franchise already exists"`
- **FRANCHISE_DOES_NOT_EXISTS**: `"The franchise does not exist"`
- **BRANCH_ALREADY_EXISTS**: `"The branch already exists"`
- **BRANCH_DOES_NOT_EXISTS**: `"The branch does not exist"`
- **PRODUCT_ALREADY_EXISTS**: `"The product already exists"`
- **PRODUCT_DOES_NOT_EXISTS**: `"The product does not exist"`
- **DATA_IS_INCORRECT**: `"The data is incorrect"`
- **UNKNOWN**: `"The error is unknown"`

Estos códigos de error se utilizan para manejar y comunicar diversos problemas que puedan surgir durante la ejecución de la aplicación.
