# Proyecto Kuntur - Pacifico La Chakra

## Iniciando

Siga las siguientes instrucciones para iniciar el desarrollo de este proyecto.

### Pre-Requisitos

#### Configuracion de Java:

* Descargar la versión 1.8 del JDk segun el sistema operativo ([lista de JDK](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html))

* Debe contar con la versión 1.8 del JDK ([como instalar el JKD en Linux](https://java.com/en/download/help/linux_x64_install.xml))


* Crear usuario en  [Artifactory](https://innovacionpacifico.jfrog.io/) (apoyarse en devOps).

#### Configuracion de Gradle:

* Debe contar con la version 6.0 o superior de gradle ([como instalar](https://gradle.org/next-steps/?version=6.0&format=bin)).

* Para **Linux**
    * descargar gradle 6
        ```jshelllanguage
        $ wget https://services.gradle.org/distributions/gradle-6.4.1-bin.zip
        ```
    * descomprimir zip
        ```jshelllanguage
        $ sudo unzip -d /opt/gradle gradle-*.zip
        ```
    * configurar variable de entorno ***GRADLE_HOME***

        * crear el archivo **gradle.sh**
            ```jshelllanguage
            $ sudo nano /etc/profile.d/gradle.sh
            ```
        * agregar variable de entorno
            ```jshelllanguage
            $ export GRADLE_HOME=/opt/gradle/gradle-6.4.1
            $ export PATH=${GRADLE_HOME}/bin:${PATH}
            ```
        * hacer el script ejecutable
            ```jshelllanguage
            $ sudo chmod +x /etc/profile.d/gradle.sh
            ```
        * cargar las variables de entorno
            ```jshelllanguage
            $ source /etc/profile.d/gradle.sh
            ```
    * verificar la instalación de gradle
        ```jshelllanguage
        $ gradle -v
        ```
    * Agregar en el archivo **~/.gradle/gradle.properties** (el archivo con las configuraciones repectivas para bajar las dependencias del Artifactory).
        ```jshelllanguage
        $ sudo nano .gradle/gradle.properties
        ```
    * Copiar y agregar la siguiente configuración
        ```properties
        artifactory_user=$USER
        artifactory_password=$ENCRYPTED_PASS
        libs_release_local=https://innovacionpacifico.jfrog.io/innovacionpacifico/libs-release-local
        plugins_release_local=https://innovacionpacifico.jfrog.io/innovacionpacifico/plugins-release-local
        ```
    * Guardar cambios con **CTRL + o**, luego **ENTER**
    * Salir de edición con **CTRL + x**

* Para **Windows**
    * descargar [gradle 6.4.1](https://gradle.org/next-steps/?version=6.4.1&format=bin)

    * abrir una terminal de **PowerShell**

    * crear el directorio **Gradle** en:
        ```powershell
        PS> mkdir C:\Gradle
        ```
    * descomprimir zip
        ```powershell
        PS> Expand-Archive -Path  $HOME"\Downloads\gradle-6.4.1-bin.zip" -DestinationPath "C:\Gradle"
        ```
    * configurar variable de entorno ***GRADLE_HOME***
        * agregar variable de entorno
            ```powershell
            PS> [System.Environment]::SetEnvironmentVariable('GRADLE_HOME', 'C:\Gradle\gradle-6.4.1',[System.EnvironmentVariableTarget]::User)
            PS> [System.Environment]::SetEnvironmentVariable('Path', $env:Path + ';%GRADLE_HOME%\bin', 'User')
            ```
        * abrir una nueva terminar y verificar la instalación de gradle
            ```powershell
            PS> gradle -v
            ```
    * Agregar en el archivo **$HOME/.gradle/gradle.properties** (el archivo con las configuraciones repectivas para bajar las dependencias del Artifactory).
        ```
        .gradle/gradle.properties
        ```
    * Copiar y agregar la siguiente configuración
        ```properties
        artifactory_user=$USER
        artifactory_password=$ENCRYPTED_PASS
        libs_release_local=https://innovacionpacifico.jfrog.io/innovacionpacifico/libs-release-local
        plugins_release_local=https://innovacionpacifico.jfrog.io/innovacionpacifico/plugins-release-local

    * Guardar cambios
#### Otras configuraciones:

Deben tener instalado Docker y Docker-Compose

---

Cómo Instalar Docker en **Linux** - [Ubuntu (LTS)](https://docs.docker.com/engine/install/ubuntu/):

* Instalar dependencias necesarias para Docker:
    ```jshelllanguage
    $ sudo apt update

    $ sudo apt install \
        apt-transport-https \
        ca-certificates \
        curl \
        gnupg-agent \
        software-properties-common
    ```
* Hecho esto ahora debemos de importar la clave GPG:

    ```jshelllanguage
    $ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    ```
* Debemos de verificar que la huella digital sea 9DC8 5822 9FC7 DD38 854A E2D8 8D81 803C 0EBF CD88, buscando los últimos 8 caracteres de la huella digital. Para ello podemos ejecutar este comando:
    ```jshelllanguage
    $ sudo apt-key fingerprint 0EBFCD88

    ```
* Ahora debemos de añadir el repositorio al sistema con el siguiente comando:
    ```jshelllanguage
    $ sudo add-apt-repository \
        "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
        $(lsb_release -cs) \
        stable"
    ```
* Hecho esto actualizamos nuestra lista de repositorios con:
    ```jshelllanguage
    $ sudo apt update
    ```
* Y ahora ya podemos realizar la instalación de Docker en nuestro sistema, sólo debemos de teclear el siguiente comando:
    ```jshelllanguage
    $ sudo apt install docker-ce docker-ce-cli containerd.io
    ```
* Para verificar que se ha instalado con éxito Docker:
    ```jshelllanguage
    $ sudo docker run hello-world
    ```
* Finalmente debemos de añadir el grupo de Docker a nuestro usuario:
    ```jshelllanguage
    $ sudo usermod -aG docker $USER
    ```
Como Instalar Docker-compose en Linux:
* Para la instalacion de Docker-compose debemos de teclear el siguiente comando:
    ```jshelllanguage
    $ sudo apt install docker-compose
    ```
Nota: Si aun su usuario no tiene los permisos para ejecutar Docker reinicie la maquina.
---

Ejemplo para levantar el api:

* Debemos de instalar algunas dependencias necesarias para el api con estos comandos:

    ```jshelllanguage
    $ gradle clean build

    ```
* En la carpeta raiz del proyecto teclear el siguiente comando para iniciar ejecutar el archivo docker-compose que permite iniciar la base de datos que usa el api:
    ```jshelllanguage
    $ docker-compose up -d db

    ```
* Para levantar el proyecto ejecutar el siguiente comando:
    ```jshelllanguage
    $ gradle bootRun
    ```
Para poder ver los end-point de los servicios se debe ir al navegador y colocar la siguente url:

[http://localhost:9091//ms-ux-distribucion-de-costos-envio-parametria-pga/v1/swagger-ui.html](http://localhost:9091//ms-ux-distribucion-de-costos-envio-parametria-pga/v1/swagger-ui.html)

***

## Tecnologías Utilizadas

![Java](https://cdn.static.innovacionpacifico.com/document_library/readme/java-logo-64.png) [JDK](https://www.oracle.com/technetwork/java/index.html)
![Spring Boot](https://cdn.static.innovacionpacifico.com/document_library/readme/spring-boot-logo-64.png) [Spring Boot](https://spring.io/projects/spring-boot)
![Spring Cloud](https://cdn.static.innovacionpacifico.com/document_library/readme/spring-cloud-logo-64.png) [Spring Cloud](https://spring.io/projects/spring-cloud)
![Maven](https://cdn.static.innovacionpacifico.com/document_library/readme/maven-logo-64.png) [Maven](https://maven.apache.org/)
![Rest](https://cdn.static.innovacionpacifico.com/document_library/readme/rest-logo-64.png) [Rest](https://es.wikipedia.org/wiki/Transferencia_de_Estado_Representacional)
![OpenApi](https://cdn.static.innovacionpacifico.com/document_library/readme/openapi-logo-64.png) [OpenApi](https://www.openapis.org/)
![Undertow](https://cdn.static.innovacionpacifico.com/document_library/readme/undertow_logo-64.svg) [Undertow](http://undertow.io/)
![Liquibase](https://cdn.static.innovacionpacifico.com/document_library/readme/liquibase-logo-64.png) [Liquibase](https://www.liquibase.org/)
![Docker](https://cdn.static.innovacionpacifico.com/document_library/readme/docker-logo-64.png) [Docker](https://www.docker.com/get-started)
![Jfrog](https://cdn.static.innovacionpacifico.com/document_library/readme/jfrog-logo-64.png) [Jfrog](https://innovacionpacifico.jfrog.io)

***

## La Chakra - Pacifico Seguros

![La Chakra](https://cdn.static.innovacionpacifico.com/document_library/readme/pacifico-logo-64.png)
