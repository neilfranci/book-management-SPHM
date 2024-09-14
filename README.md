# big-project-group-6

Big Project about Book Management with Java OOP

- bgsix: Is the team name: Big Group Six (nhom 6)
- SJPM: Spring JavaFX Postgresql Maven (the technology stack)

<div align="center">
  <span>English</span> |
  <a href="https://github.com/neilfranci/book-management-SJPM/blob/main/translation/README_vi_VN.md">Vietnamese</a>
</div>
<br/>

## Table of Contents

### 1. Pre-requisite for Windows

   1. [Install the Postgresql](#1-install-the-postgresql-download)
   2. [Adding necessary program to path](#2-adding-necessary-program-to-path)
   3. [Creating a database](#3-creating-a-database)
   4. [Importing the database](#4-importing-the-database)
   5. [Install maven](#1-install-maven)
   6. [Install GraalVM native-image](#2-install-graalvm) (Java SDK replacement)
   7. [Install Pre-requisite for GraalVM native-image (Production)](#3-install-pre-requisite-for-graalvm-native-image-to-build-the-production-build)

### 2. Setup Project

1. [Clone the project](#1-clone-the-project)
2. [Open the project in your favorite IDE](#2-open-the-project-in-your-favorite-ide)

### 3. [Run the Client](#3-run-the-client)

1. [Running the client (Development)](#31-running-the-client-development)
2. [Running the client (Production)](#32-running-the-client-production)

### 4. [Run the Server](#4-run-the-server)

1. [Open the .env.template file in this path](#41-open-the-envtemplate-file-in-this-path)
2. [Running the server (Development)](#42-running-the-server-development)
3. [Building the native image (Production)](#43-building-the-native-image-production)

---

## Postgresql installation for Windows

### 1. [Install the Postgresql (download)](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)

1.1 After press install, and next.

1.2 You now in "Select Components" part

1.3 Uncheck Stack Builder

![alt text](assets/psql-install-1.png)

1.4 press next, until you meet password for superuser
type your own password

`default port is 5432`

1.5 keep pressing next until it in "Installing" part.

1.6 Wait for it done, and Finish. DONE!

### 2. Adding necessary program to path

Default install path is:

```pwsh
C:\Program Files\PostgreSQL\16\bin
```

Open system enviroment variable on Windows.

<img src="assets\enviroment-variable-1.png" height="250">

<img src="assets\enviroment-variable-2.png" height="400">

- Double click on the Path in the system section

<img src="assets\system-path.png" height="200">

- Click on new
- Paste the path
- CLick OK

<img src="assets/add-path.png" height="400">

### 3. Creating a database

#### 3.1. Verify Path is added to the enviroment

- Open the command prompt or powershell

```pwsh
psql -V

# Output : psql (PostgreSQL) 16.4
```

#### 3.2. If the previous command is return like above then continue with next part

- Create a database

```pwsh
createdb -U postgres book-management
```

enter your password from [#1.4](#1-install-the-postgresql-download)

> We using the default superuser "postgres" here

#### 3.3 Verify dababase is created

```pwsh
psql -U postgres

# then in the console
postgres=#

# type \l (not the number 1)

postgres=# \l


```

> \q to quit the console or Ctrl + C

- Example output

<img src="assets/psql-l.png">

### 4. Importing the database

- Open the Terminal in the project folder (root) [follow this](#setup-project)
- The book-management.sql is in the root of the project (using relative path to import the database)

- If `book-management.sql` is in the root of the project then run this command

```pwsh
psql -U postgres -f .\book-management.sql book-management
```

`---------^username----^file path----------- ^database name`

- Verify the database is imported

```pwsh
psql -U postgres -d book-management

# then in the console

SELECT author FROM book LIMIT 5;      # to see the data
```

### 1. Install maven

- Download maven from <https://maven.apache.org/download.cgi>
  - Binary zip archive apache-maven-3.9.9-bin.zip
- Extract the zip file to the desired location
  - Example: `C:\apache-maven-3.9.9` (make sure there is a folder `bin` in the `apache-maven-3.9.9` folder)
- Add the maven path to the system environment variable
  - Open the environment variable
  - Double Click on the `Path` in the user section
  - Click on new
  - Paste the path `C:\apache-maven-3.9.9\bin`
  - Click OK

- Verify the maven installation

```pwsh
mvn -v
# Output : Apache Maven 3.9.9
```

### 2. Install GraalVM

- Download GraalVM from <https://www.graalvm.org/downloads/>
  - Make sure to choose Java 22 version and Windows 64 bit
- Extract the zip file to the desired location
  - Example: `C:\Program Files\Java\graalvm-jdk-22.0.2+9.1\bin` (make sure there is a folder `bin` in the `graalvm-jdk-22.0.2+9.1` folder)
- Add the GraalVM path to the system environment variable
  - Open the environment variable
  - Double Click on the `Path` in the user section
  - Click on new
  - Paste the path `C:\Program Files\Java\graalvm-jdk-22.0.2+9.1\bin`
  - Click OK

- Verify the GraalVM installation

```pwsh
java -version
# Output : 
# java version "22.0.2" 2024-07-16
# Java(TM) SE Runtime Environment Oracle GraalVM 22.0.2+9.1 (build 22.0.2+9-jvmci-b01)
# Java HotSpot(TM) 64-Bit Server VM Oracle GraalVM 22.0.2+9.1 (build 22.0.2+9-jvmci-b01, mixed mode, sharing)
```

### 3. Install Pre-requisite for GraalVM native-image to build the Production build

- Follow the instruction from <https://www.graalvm.org/latest/docs/getting-started/windows/#prerequisites-for-native-image-on-windows>

## Setup Project

### 1. Clone the project

```bash
git clone https://github.com/neilfranci/book-management-SJPM.git
```

### 2. Open the project in your favorite IDE

2.1 VSCode

cd to the project folder

```bash
cd book-management-SJPM
```

then open the project in vscode with this command

```bash
code .
```

install vs code extension for java <https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack>

then wait for the extension to install

```bash
mvn clean install
```

### 3. Run the Client

#### 3.1 running the client (Development)

```bash
mvn -pl client javafx:run
```

#### 3.2 running the client (Production)

```bash
mvn -pl client javafx:jlink
```

then run the executable file in the target folder
`.\client\target\client\bin\Client`

the production build is in the `.\client\target\client` folder
and also the zip file in the `.\client\target` folder

### 4. Run the Server

#### 4.1 open the `.env.template` file in this path

```bash
server\.env.template
```

- create a new file `.env` in the same path and copy the content from `.env.template` to `.env`

- replace the value with your own value

#### 4.2 running the server (Development)

```bash
mvn -pl server spring-boot:run
```

#### 4.3 building the native image (Production)

```bash
mvn -pl server -Pnative native:compile -DskipTests
```

- then run the executable file in the target folder
`.\server\target\server` (the native image is a single file) in this case the file is `server.exe`

---

## FAQ

### 1. Cannot drop the currently open database, database "book-management" already exists

Answer: Just drop the database

```pwsh
dropdb -U postgres book-management
```

### 2. DTO? What is that?

Answer: Data Transfer Object (DTO) is a design pattern used to transfer data between data access objects and value objects. The data is transferred in the form of data transfer objects. DATABASE Class > Repository Class > Service Class > Controller Class > DTO Class > Sendalbe Data to Client
