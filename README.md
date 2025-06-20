# demo-registro-usuarios

Demo de aplicación de gestión de usuarios.

Utiliza las siguientes tecnologías.

- Springboot (v2.7.18).
- Java (jdk-11.0.0.2).
- Maven como gestor de dependencias y para generar build (v3.9.10).
- Persistencia JPA con Hibernate.
- Banco de datos persistente con H2.
- Swagger.



# Pasos Previos
Previo a la descarga del servicio, es necesario contar con las siguientes herramientas y configuraciones:

- Visual Studio Code (Editor utilizado para el desarrollo).
- Postman (Para probar ejecuciones sobre los endpoints).
- Java version 11.0.0.2 (con variable JAVA_HOME configurada).
- Maven version 3.9.10 (con variable de entorno configurada).

Dado que el desarrollo se llevo a cabo en Visual Code, también es necesario contar con las siguientes extensiones:

![image](https://user-images.githubusercontent.com/32346999/187487347-566da33e-8c57-4474-bd70-31423ef3bbf0.png)




# Descarga e Inicio de la Aplicación

El primer paso consta de clonar el repositorio en un directorio local, para posteriormente abrir el proyecto en vscode, abrir una terminal y ejecutar el siguiente comando: 

**mvn spring-boot:run**

Validamos el correcto inicio de la aplicación en la terminal:

![image](https://github.com/user-attachments/assets/253a7cf8-5c30-4f3d-a68b-a725d43a3bb8)

**IMPORTANTE:** Dentro de la carpeta del proyecto, en la carpeta "complementos" se encuentra la tanto la colección de Postman como los diagramas de la solución.

![image](https://user-images.githubusercontent.com/32346999/187561851-f7ffe6a4-7b75-4f33-bb54-02b9de567b55.png)



# Ejecución de Endpoints

## Ingreso de Usuario

Para la validación de formato del email y la password, se especificaron las expresiones regulares en el archivo application.yaml de tal modo que sean configurables, en el caso de la contraseña, esta debe contar con las siguientes condiciones:
- Minimo una letra minuscula.
- Minimo una letra mayuscula.
- Minimo un numero.
- Minimo un caracter especial.
- Minimo 5 de largo.

![image](https://user-images.githubusercontent.com/32346999/187508776-a71fd382-e32c-448a-829a-207cf695f425.png)

Procedemos a porbar el enpoint "Ingresar Usuario", para esto hacemos unas primeras pruebas tanto con un nombre e email invalidos.

![image](https://github.com/user-attachments/assets/3d03e829-8125-429f-89bd-fd3b51e27799)

![image](https://github.com/user-attachments/assets/ca24035a-2d2b-4c9c-bcf4-a063bea2560f)

Hacemos una segunda prueba, esta vez utilizando solo una password invalida.

![image](https://github.com/user-attachments/assets/48a3d537-a71e-443f-801a-90aa743e389c)

![image](https://github.com/user-attachments/assets/068557cb-eae7-4540-98b3-a0843c033062)

Volvemos a intentar el registro, esta vez con un email y password validos. Visualizamos el correcto registro del usuario con codigo HTTP 201 CREATED.

**IMPORTANTE:** Considerar el id de usuario y token asociado.

![image](https://github.com/user-attachments/assets/518b70e7-6326-477f-aec4-e409a242efd2)


## Buscar Usuario por ID

Mediante el endpoint "Buscar Usuario" de la colección postman, podemos buscar el usuario recién creado. Para ello copiamos el ID entregado en el registro y lo cargamos como un PATH Variable en la url.

El servicio responde con la información del usuario:

![image](https://user-images.githubusercontent.com/32346999/187519497-66e64816-c38f-4982-8141-9f4b3266585c.png)

Podemos intentar nuevamente el registro para detonar la excepcion de que email ya se encuentra registrado.

![image](https://user-images.githubusercontent.com/32346999/187520710-acd339f0-9eab-4a6a-b108-577c9f1f9a46.png)


## Modificar Usuario

Con el endpoint "Modificar Usuario" modificamos el usuario recien ingresado, identificando el ID en la url.

![image](https://github.com/user-attachments/assets/74a30f28-8697-47fd-9208-0f4a0e287d0f)


## Cambiar Estado Usuario

Se intenta activar un usuario ya activo: 

![image](https://github.com/user-attachments/assets/a3fcc275-a249-42a3-91c8-9cd14522c0d8)

Se cambia estado del usuario:

![image](https://github.com/user-attachments/assets/49c1d63c-876e-45c5-a3c0-56725429568e)


## Eliminar Usuario

Se creo nuevo usuario para posteriormente ser eliminado:

![image](https://github.com/user-attachments/assets/3daa294d-10b0-4114-a800-2f603e109b31)

![image](https://github.com/user-attachments/assets/ee36454d-70a1-45a0-b7bf-bde72df7f14c)


## Login

Intento de login con usuario inactivo:

![image](https://github.com/user-attachments/assets/625bd077-ccb4-4668-83c0-dc1df47d91dd)

Login con usuario valido:

![image](https://github.com/user-attachments/assets/4cd973ec-3f57-416b-b4f5-482b6d079da7)


## Listar Usuarios

Se habilitó endpoint utilizatio para listar todos los usuarios creados (ejecutar con token jwt valido):

![image](https://github.com/user-attachments/assets/21e5bb6a-4e62-49be-93bb-e558989b7823)



# Acceso a consola H2.

Para acceder a la consola de H2, cargamos la siguiente ruta en el navegado http://localhost:8001/h2-console/login.jsp e introducimos los siguientes datos de conexion:

![image](https://github.com/user-attachments/assets/4f9e5157-87da-4661-99d1-31d6e1f3be8e)

Podemos visualizar los usuarios creados:

![image](https://github.com/user-attachments/assets/b36b07db-2aa9-4d24-81b1-c2f58e312140)



# Swagger

Para visualizar el swagger el servicio, accederemos a la interfaz cargando el numero de puerto en un navegador.

En este caso la url sera: http://localhost:8001/swagger-ui/index.html

![image](https://github.com/user-attachments/assets/8f912a19-3039-4445-8562-c34c3e97fad4)
