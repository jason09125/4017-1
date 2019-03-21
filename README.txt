This is a simple client-server based chatting system.  It was developed
using Java.  In this package, there are two sub-directories:

  - server -- source code to the chat server
  - client -- source code to the tty-based chat client


#===============================================================================
# Server
In the "server" directory, the following files are provided:

  - ChatServer.java
  - ChatServerThread.java

The chat server is tty-based (i.e., it should be run in text-mode).
You should compile the above two java source files first, and start the
server by doing the following at a command prompt:

  java ChatServer port_num

Note that "port_num" is the port number to be used by the server waiting
for chat clients to connect to it.

Suppose the port number is 5432, the following would be shown:

  | Binding to port 5432, please wait  ...
  | Server started: ServerSocket[addr=0.0.0.0/0.0.0.0,port=0,localport=5432]
  | Waiting for a client ...


#===============================================================================
# Client
In the "client" directory, the following files are provided:

  - ChatClient.java
  - ChatClientThread.java

You should compile the above two java source files first, and start the
client by doing the following at a command prompt:

  java ChatClient host port_num

Note that "host" is the name of the host where the chat server is running,
and "port_num" is the port number be used by the server waiting client
connection.  For example, the following would invoke the chat client to
connect to the chat server running on the localhost with port 5432.

  java ChatClient localhost 5432

By running the above, something similar to the following would be shown:

  | Establishing connection. Please wait ...
  | Connected: Socket[addr=localhost/127.0.0.1,port=5432,localport=52218]

If you start a number of chat clients (on different ttys, of course), the
clients would be able to chat through the chat client.
