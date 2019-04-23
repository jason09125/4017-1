## Instructions

This section shows the instructions for running and termination.

> All commands are for Mac OS (Linux), if you are using Windows, please use the corresponding commands in Windows.

### Creating a Java package (.jar) for distribution

> This project manages dependencies using **[Maven](https://maven.apache.org)**, it's required to have Maven installed to build (compile) the program

1. Install Maven
1. `cd` to the root directory of this repository
1. Run `mvn package`, the package should be ready in `./target` with a name like `JavaChat-1.0.jar`

### Using a package prepared for you

For those who don't install Maven, a package of the latest version is prepared for you at `./build/JavaChat-1.0.jar`

### Running Server

> Please ensure you have put the **server config file** properly in the project before running, it should be placed in **`./server-config/config.properties`**

1. Ensure the server config file is ready in `./server-config/config.properties`
1. Go to the directory where you have the JAR package ready
1. Run server on a port, say 6000: `java -cp JavaChat-1.0.jar server.chat.ChatServer 6000`

> Or use `mvn exec:java -Dexec.mainClass="server.chat.ChatServer" -Dexec.args="6000"` to run directly with Maven in root directory

### Running Client
1. Make sure server is already up, say at `localhost` and on port `6000`
1. Get your client config file ready, say at the same path as the JAR package named `eric.properties`
1. Go to the directory where you have the JAR package ready
1. Run: `java -cp JavaChat-1.0.jar client.chat.ChatClient localhost 6000 eric.properties`

> Or use `mvn exec:java -Dexec.mainClass="client.chat.ChatClient" -Dexec.args="localhost 6000 ./client-config/eric.properties"` to run directly with Maven in root directory

### Terminating

For both client and server programs, input `Ctrl + C` in the terminal to terminate it
