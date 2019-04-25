## User manual

Detailed steps of starting, logging in, chatting, creating a new user will be provided in sections:
- **Instructions of building and running**
- **Instructions of logging in, chatting and creating new user** 

### Log in from client (signing on)

1) Input server IP, port and name of the client .property at the Login Window

2) Inupt client name, password and token at the Token Window

> token is a time-based one time password (OTP), you should retrieve it from an app like Google Authenticator

#### Pre-register users:

| Username      | Password    | Two-factor Auth Secret  |
|:------------- |:----------- |:----------------------- |
| Eric          | 123456      | 4ZC2262UODCPBI3A        |
| Thomas        | qwerty      | WOGH23LCOOSWQ7TP        |
| Joe           | 123456      | 6MP6EPHBCYI4POYC        |

> You should use the above 2FA secret to set up a time-based OTP authenticator like Google Authenticator first.

> Their public keys and private keys are stored in corresponding file in `./client-config` 

### Quit the program from a client (signing off)

Close the GUI window and click YES button at the pop-up window

### Get config files ready

#### Server config file

Set `SERVER_MASTER_PUBLIC_KEY` and `SERVER_MASTER_PRIVATE_KEY`, values are in Base64 encoded string format.

There's a single config file for the server.

Example:
```
SERVER_MASTER_PUBLIC_KEY=MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYnjpkmNdf/jJV68UxUn4Sh0i8w51Sf2M1jd3t/lJw4ccss5FshsYFX/168sIoIW2IQn91B5PUXgy6lPkBME3I13MQknYIPp4aNyhLzeIV+8bqeNnxnSgFNttfgo/ygDu2nnASWddkaL3/LWIbv8FFgeN7+CQx2hcCW99zxR0GNQIDAQAB
SERVER_MASTER_PRIVATE_KEY=MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJieOmSY11/+MlXrxTFSfhKHSLzDnVJ/YzWN3e3+UnDhxyyzkWyGxgVf/XrywighbYhCf3UHk9ReDLqU+QEwTcjXcxCSdgg+nho3KEvN4hX7xup42fGdKAU221+Cj/KAO7aecBJZ12Rovf8tYhu/wUWB43v4JDHaFwJb33PFHQY1AgMBAAECgYAgYrs7a5+QdC2URALFU58DKYgK3mu8/OE9lQw6G5S89XxBhR1f7T2KGHN+qpL+1xEaMBpB6Ei7cPW8hi5MzUZD+obTxgHUKMUjM3HmQPv3EAji+vEztB4g6Mb0ixPB3LEExKkPFR4pWByVxbaS1S1Sm6dODcfoSMnWLwmGq9PLJQJBAOR39kcAOQ3g5XRiVhFLlXe4z+RpJlLc3gA8gHbFKx1SRkXi0dr/E9jMIIlYi/Dnwx0kiuHqbaCfg35sCMfhXqcCQQCrAlmVoiH6gPBYk2FaGeEHal1LJ68evktxKRdM4JM1uPGYJ5ImbUg360ghqmHieD7WLXNI9XQGxNiyyVVacUvDAkEA2cXzTYhL2gvGC6L0UTYPuffCygDkk9WOEwGYnh2g1CkpbNIgoLPFMkCYvvJVKgNweyXq8B7p8lI6H9ZQpF4RPwJAQ5++RUnEHgd3A8/kI3kwX3pUQjADNCkUND+Hk0MLc6cbAoxDYya79ED6WTXDV4ctcgyvFh7aLwMQnkK3mfhLeQJAU0zwjbW3VBbKGzv5A/53L937LiV8qGD02u4LVPY0+I04mvUJyekbJYB2iVrLebJjRDyPkL3ZEJCHhhWAeoskLw==
```

> This repository already prepares one for you, which is `./server-config/config.properties`

#### Client config file

Set `SERVER_MASTER_PUBLIC_KEY`, `CLIENT_MASTER_PUBLIC_KEY` and `CLIENT_MASTER_PRIVATE_KEY`, values are in Base64 encoded string format.

There are one config file for each of the client / user.

Example:
```
SERVER_MASTER_PUBLIC_KEY=MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYnjpkmNdf/jJV68UxUn4Sh0i8w51Sf2M1jd3t/lJw4ccss5FshsYFX/168sIoIW2IQn91B5PUXgy6lPkBME3I13MQknYIPp4aNyhLzeIV+8bqeNnxnSgFNttfgo/ygDu2nnASWddkaL3/LWIbv8FFgeN7+CQx2hcCW99zxR0GNQIDAQAB
CLIENT_MASTER_PUBLIC_KEY=MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOPPgcC4mC26zC3tCt8TbRqRYC2t1Swm5bmAW9jBWhWxlAqUXm27ASUGo1YhJnAh1u84O1dmoMcY8KUtZtJF/7Fw6IpcWWHu1g45bhI+nKgkjV2BxrgcgSiUX8ms6txb470Iep/0NkRZn9x3TbifWQqxjH2FMVeV9OhUfmSvXI1wIDAQAB
CLIENT_MASTER_PRIVATE_KEY=MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI48+BwLiYLbrMLe0K3xNtGpFgLa3VLCbluYBb2MFaFbGUCpRebbsBJQajViEmcCHW7zg7V2agxxjwpS1m0kX/sXDoilxZYe7WDjluEj6cqCSNXYHGuByBKJRfyazq3FvjvQh6n/Q2RFmf3HdNuJ9ZCrGMfYUxV5X06FR+ZK9cjXAgMBAAECgYB0ev9f0B7jV8xJpThVSbTvyz0oR1152ZmQTpVc3SwVgEnUxwpkfMHarZncb5zMWFIMO0U/xGIiIJjYBnBs3p3t+XJyiaZ/J6D6YaTW/3dc1cwBtyUiPIWdBdQEQMni12OJrUiQHzeZPNvk1r3+ZNde+WVmNsgLGCRUKnlV91KLyQJBAPfZUvZ3Kuz486YhqGVLJeUJyVOs464pZHO0NFe634G1ImmcWrpmXIg2JHfwYMFLeodh99lJRUooa9ia5QECXUUCQQCS6n4qBf4HX1/WlmTQH7GAgM4tgOapBRa/8roYmtTPEa9T/aJL2k3tiGXcGphQyVI5KHzayj/DTRfO6Y2UkelrAkAWajIllhtsuQsYAD1Bg+1WbG8nwSAKNTYffLGrKXxjN6V4Fari5rUBoJvluPiXIqNfMQ4AOa8piMRQH5oMYFFdAkBGAS50374XzT5hhfArq65syPN1g0Jlr2MTu5kpOD3HHWop32WCN1eCo8fFhXamqAdh7QTxTAXuDcIWeftYm95ZAkEAhvP8l+HhqJGylxssqJ3OpGtCPD+yNGSpkejIH+MpajIv8cGeKRSg00uXM2G4nx+iWHuMvO1+aBYaJyxLT5N4pg==
```

> This repository already prepares three for you in directory `./client-config`, for three different registered users

### Instructions of building and running

This section shows the instructions for running and termination.

> All commands are for Mac OS (Linux), if you are using Windows, please use the corresponding commands in Windows.

#### Creating a Java package (.jar) for distribution

> This project manages dependencies using **[Maven](https://maven.apache.org)**, it's required to have Maven installed to build (compile) the program

1. Install Maven
1. `cd` to the root directory of this repository
1. Run `mvn package`, the package should be ready in `./target` with a name like `JavaChat-1.0.jar`

#### Using a package prepared for you

For those who don't install Maven, a package of the latest version is prepared for you at `./build/JavaChat-1.0.jar`

#### Running server

> Please ensure you have put the **server config file** properly in the project before running, it should be placed in **`./server-config/config.properties`**

1. Ensure the server config file is ready in `./server-config/config.properties`
1. Go to the directory where you have the JAR package ready
1. Run `java -cp JavaChat-1.0.jar server.chat.ChatServer' and then input the port number you want. 
P.S. (The server IP is defalt as your IP)

> Or use `mvn exec:java -Dexec.mainClass="server.chat.ChatServer"` to run directly with Maven in root directory

#### Running client

1. Make sure server is already up, say at `localhost` and on port `6000`
1. Get your client config file ready, say at the same path as the JAR package named `eric.properties`
1. Go to the directory where you have the JAR package ready
1. Run: `java -cp JavaChat-1.0.jar client.chat.ChatClient'

1) Input server IP, port and name of the client .property at the Login Window

2) Inupt client name, password and token at the Token Window

> token is a time-based one time password (OTP), you should retrieve it from an app like Google Authenticator

> Or use `mvn exec:java -Dexec.mainClass="client.chat.ChatClient" ./client-config/eric.properties"` to run directly with Maven in root directory

#### Terminating

For both client and server programs, close the GUI window and click YES button at the pop-up window to quit the program.

### Instructions of logging in, chatting and creating new user

#### Logging in a user
1. Run client program according to the instructions above
1. See the *"safe to log in"* notice on the client program (for server authentication)
1. Get the one time password (token) from your authenticator app (e.g. Google Authenticator)
1. Inupt client name, password and token at the Token Window

#### Chatting
1. Send message as usual
1. You will see all the authenticity and integrity check along the way

#### Creating a new user

1. Follow the steps for building (or use the prepared Jar package for you)
1. A key pair generator for registration is ready for you, run this: `java -cp ./target/JavaChat-1.0.jar client.auth.RegistrationHelper`
1. Save the public key and private key (perhaps save it in a .properties file, as you will use it to login later)
1. Run `java -cp ./target/JavaChat-1.0.jar server.user.UserManager {username} {password} {public-key}`
1. Retrieve the *two-factor authentication secret* (something looks like `A2PUWWNZZ43Y7MON`) and put it in a time-based OTP authenticator (e.g. Google Authenticator)  

> Note that we specifically require generating key pair on client side (in client package) and registering on server side (in server package) for security reasons.

> Note that you can use any key pair generator, as long as it's a RSA key pair. We just provide one generator for you for your convenience, it does not mean that you must use it.

## Implementation & Design Details

### Project File structure
Let's look at our file structure first and we will go through each part of it in detail.

```
┌── client
│   ├── auth
│   │   ├── ClientAuthenticator
│   │   ├── PublicKeysStorage
│   │   └── RegistrationHelper
│   │
│   ├── chat
│   │   ├── ChatClient
│   │   └── ChatClientThread
│   │
│   ├── ClientGUI
│   │   ├── ClientChatWindow
│   │   ├── ClientLoginWindow
│   │   └── ClientTokenWindow
│   │
│   ├── image
│   │
│   └── message
│       └── MessageHandler
│
├── server
│   ├── auth
│   │   └── ServerAuthenticator
│   │
│   ├── chat
│   │   ├── ChatServer
│   │   └── ChatServerThread
│   │
│   ├── db
│   │   ├── DatabaseMock
│   │   └── FileManager
│   │
│   ├── group
│   │	└── GroupHandler
│   │
│   ├── message
│   │   └── MessageHandler
│   │
│   ├── ServerGUI
│   │   ├── ServerSetupWindow
│   │   └── ServerWindow
│   │
│   └── user
│       ├── User <<implements Serializable>>
│       └── UserManager
│
└── shared
    ├── AsymmetricCrypto
    ├── DataConverter
    ├── Md5Helper
    └── SymmetriCrypto
```

Mainly the it is separate into three parts, client package, server package and shared package. Note that for deployment in production, we should create builds and deploy client and server separately, but for simplicity in this project, we make a build containing both of them.

#### Client side package
- `auth.ClientAuthenticator` deals with key management (storing public key,  private key and session key) for the client instance, and it also signs the challenge sent from the server.
- `auth.PublicKeysStorage` manages other users' public keys, it supports caching received public keys in client side local file system.
- `auth.RegistrationHelper` indeed is a key pair generator, it simply provides a main function generating key pair for new user to sign up. Note that new user can use any RSA key pair generator, we just create such class for convenience.
- `chat.ChatClient` is provided to us, which we do not change too much. We only add some new command and response handling logic in it. For the detail of what and how commands and responses are handled, please see section *Workflow Breakdown*.
- `chat.ChatClientThread` is provided to us, and we make no changes except for several lines of logging in this file.
- `message.MessageHandler` decrypts and verifies the incoming message from the server, but also encrypts and signs the message sending to the server.
- `ClientGUI.ClientLoginWindow` providing a GUI for the user to input the server IP, port number and the property file name.
- `ClientGUI.ClientTokenWindow` roviding a GUI for the user to input his/her user name, password and token.
- `ClientGUI.ClientChatWindow` providing a GUI for the user to chat.
- `image` is the folder that holds the images of the GU.

#### Server side package
- `auth.ServerAuthenticator` signs data for the server using *Sever Master Private Key*, including challenge sent from client, session key and public key (see section Workflow Breakdown for further detail). It also reads server config file to get the private key.
- `chat.ChatServer` is provided to us, and we do not change too much. We only add some new command and response handling logic in it. For the detail of what and how commands and responses are handled, please see section *Workflow Breakdown*.
- `.chat.ChatServerThread` is provided to us, and we make no changes except for several lines of logging in this file.
- `db.DatabaseMock` mocks a database for persisting user information (in server side local file system). For detailed information, please see the following *Database Mock* section below.
- `db.FileManager` helps mock database to deal with local file system and object serialization/deserialization.
- `message.MessageHandler` decrypts and verifies the incoming message from a client, but also encrypts and signs the message sending to the other clients.
- `user.User` is a serializable class for mirroring user objects, where username, password hash (see the below sections to understand why it's hashed), public key and two-factor authentication secret are stored. For more information, please see the following *Database Mock* section below.
- `user.Manager` deals with client/user authentication for the server, and it generates a client session key for each authenticated client. Note that it also provides register function for new user on the server side. You should run this separately on server, see *User Manual* section for more information about how to create a new user.

#### Shared package
- `AsymmetricCrypto` is a utility class dealing with signing, signature verifying, asymmetric encryption/decryption.
- `DataConverter` helps convert byte array to base64 string and vice versa. It also converts java.security.Key instance to base64 string. For more information, see *Data Type Conversion* section below.
- `Md5Helper` digests a piece of data and generates the MD5 checksum of it, and also checks the integrity of data by verifying whether or not a piece of data hashes to the provide checksum.
- `ServerGUI.ServerSetupWindow` is for entering the port of the server.
- `ServerGUI.ServerWindow` is for display the encrypted messages between clients.

> Even though client and server share it in this setup, in production environment client and server are deployed separately and hence the shared package should also be deployed twice with client and server respectively (for security, architect and maintenance concerns).

### Database Mock
To persist our user information, while at the same time make things simple to focus on security and cryptography design, we use server's local file system (directory path: `/db`) to replace traditional database. This indeeds mock a very basic database. For easy understanding, we call such method of persistent storage Database Mock.

User are stored in serializable objects with the following fields:
1. `username` (String, unique, primary key)
2. `passwordHash` (String)
3. `publicKey` (String)
4. `tfaSecret` (String)

Our implementation ensures username is unique, and both username and password are case-sensitive. Note that we use *BCrypt* algorithm to generate the hash of the password and store it in our Database Mock. Note that we don't store the password plaintext on purpose to prevent password leakage (even the database admin itself cannot see the password). Besides, we specifically use BCrypt but not other hash function such as MD5, to ensure that each password is encrypted several rounds with salting, and hence to prevent *rainbow table attack*.

Note that the User class implements Serializable interface to support object serialization (for writing to file) and deserialization (for reading from file).

### Data Type Conversion
Note that in most of the cases, key and cipher text are in byte array in Java, so we need some conversion methods to convert between byte array and string. We chose a standard encoding format called *Base64* to convert byte array to string and vice versa. More detail can be seen in class `shared.DataConverter`

### Crypto Algorithms
- We apply `RSA` for digital signature (signing data) and asymmetric encryption (encrypting session key).
- We apply `AES` for symmetric data encryption (session key is a key under AES, we use it to encrypt messages).
- We apply `BCrypt` to hash password for each user (prevent password from being read in case of database leakage and *rainbow table attack*).
- We use time-based one time password implemented in `com.warrenstrange.googleauth.GoogleAuthenticator` to achieve two-factor authentication.
- We use `MD5` as a way to generate checksum for data. This way we can ensure the integrity of the data.


## Workflow Breakdown
This section breaks down the workflow our the system into several phases.

### Registration Phase
This phase is down by `server.user.UserManager` main function. See the previous section *User Manual* for detail and usage.

Step:
1. New user generate its kay pair (could use `client.auth.RegistrationHelper`, see section *User Manual* for detail and usage) and gets the public key ready
1. New user inputs username, password and public key
1. New user receives the two factor authentication token (shown on screen for only once)
1. New user stores the token in an authenticator app (e.g. Google Authenticator)

### Server Authentication Phase
In this phase, client checks the identity of the server (before logging in) by sending a challenge to the server. This prevents client from sending login credentials to a forged server.

Steps:
1. Client generates a random string (in our implementation, it’s a random number) and send server authentication command to the server.
*Server auth command:* `COMMAND SERVER_AUTH {random_string}`
2. Server receives the command and uses *Server Master Private Key* to sign the random string and send back the digital signature to the client.
*Server signature response:* `RESPONSE SERVER_SIGNATURE 200 {signature}`
Note that 200 is the status code, in our program the meaning of our status codes is similar to HTTP status code, for simplicity and ease of understanding.
3. Client verifies the signature from the server using *Server Master Public Key*.

### Client Authentication Phase
In this phase, client logs in the server to use the chatting service (receiving and sending messages).

Client authenticates itself via the following four pieces of data:
1. Username
2. Password
3. Token (one time password, aka OTP)
4. Digital signature
> Note: the first three are entered by user itself, while the digital signature is generated by the client program on its own.  

> Terminology: `client` means the client portal, which is a program - the chatting app, while `user` means the person who is using the program.  

Steps:
1. Client connects to the server on a specific port at a specific host.
2. Server accepts the connection and immediately sends a challenge (a random string) to the client.
*Challenge command:*`COMMAND CHALLENGE {random_string}`
4. Client receives the challenge and stores it in memory, waiting for user to enter username, password and token.
5. User inputs its username, password and token.
6. Client receives user's input and uses the pre-provided *Client Master Private Key* to sign the challenge received previously from the server - digital signature generated.
7. Client sends the username, password, token and digital signature to the server.
*Login command:*`COMMAND LOGIN {username} {password} {token} {signature}`
8. Server checks the credentials (password, token and signature), and if they are all matched, server returns a response to the user with status code 200 indicating a successful login. For simplicity, we omit some other returned values (shown as ...) for now.
*Auth response*:`RESPONSE AUTH 200 {username} ...`

For unsuccessful login, server responds correspondingly.
* Incorrect credentials (password/token/signature): `RESPONSE 403 Invalid_credentials` 
> We on purpose do not expose further information which part of the credentials is incorrect to the client)  

* User does not exist: `RESPONSE 403 Invalid_credentials`
> We make this the same as incorrect credentials on purpose, to avoid malicious parties to check whether or not a user exists  

* User already logged in (user is online): `RESPONSE AUTH 409 Already_logged_in`
> We prevent multiple logins on purpose. Meanwhile, we notify the logged in user that there is another login attempt (by sending server message: `SERVER_MSG===Server noticed and blocked a login attempt of your account from another client portal`; The user who attempts the login also knows that the account is logged in, by seeing 409 returned status code. This way, the login condition and attempt of the account become transparent to both parties. We do consider this is a situation both parties should be notified, as one of the two parties may be the real user and should notice this situation.  

### Session Key Distribution Phase
This phase follows right after the *Client Authentication Phase*. Session key is a symmetric secret key for **end-to-end encryption between client and server**. This phase server generates and distributes the session key to an authenticated client. This key is used per session and is discarded right after each session, and each server-client pair has different session key. We define each login as a session, so the session key is only valid for the current time of login. In other words, once a client (or a user) logs out, the session key will no longer be used, and a new one will be generated the next time the user logs in.
> Note that it's NOT end-to-end encryption between one client and another, you will get better sense of this when you keep reading.  

Steps:
1. After *Client Authentication Phase*, the server generates a new session key (using AES algorithm) for the specific authenticated client (referred as "*the client*" below).
2. Server saves the session key in its memory.
3. Server encrypts the key using the client's *Client Master Public Key* (asymmetric encryption)
4. Server sends the encrypted session key to client as the response to client's *login command* (see *Client Authentication Phase*). For simplicity, we omit some other returned values (shown as ...) for now.
*Auth response:*`RESPONSE AUTH 200 {username} {encrypted_session_key} ...`
5. Client receives the encrypted session key and decrypts it using *Client Master Private Key*, and then stores it in memory.
> The key will be used in *Communication Phase* for data encryption and decryption.  

### Client Public Key Distribution Phase
Before we go through the phase of sending and receiving message, we would like to talk about client public key distribution first. In this phase, server distributes other clients' public keys respectively to each client.

A client needs another client's public key because our message encryption procedure involves data signing (for non-repudiation and authentication sake). And to verify the signature of the other client, the public key of the other client is necessary. Hence, we introduce this particular phase to handle this issue.

Steps:
1. Server sends a list of current online users to the newly authenticated client. This is done together with the *auth response* mentioned earlier.
*Auth response:*`RESPONSE AUTH 200 {username} {encrypted_session_key} {list_of_online_users}`
2. Client receives the auth response and gets the list of online users, the client then check the cache whose public keys it does not have, then user sends a request to the server to get the public keys of those users.
*Public key request command:*`COMMAND PUB_KEY_REQUEST {list_of_users}`
> As there could be many clients, for better performance, we do the distribution in a lazy manner and we support persistent cache of the received public keys. In such design, client only requests public keys of the online users whose public keys are not already cached.  
3. Server responds with the public key of each of the user on the list sent from the client respectively. Server also signs the user's public key with its *Server Master Private Key* and generates MD5 checksum for the public key.
*Public key response:* `RESPONSE PUB_KEY {username} {public_key} {signature} {checksum}`
4. Client receives the public key (note that it's in plain text) and verifies the signature and checksum, once it's confirmed to be a valid and trusted public key of integrity, client stores in both memory and persistent cache.
> Once the public key is received and verified, it will be stored persistently locally, so that it won't be requested next time. Note that as public key distribution is a critical phase, so we introduce server signature to ensure and build trust.  

### New Comer Public Key Broadcasting Phase
This phase is a complement to the previous *Client Public Key Distribution Phase*. In this phase, server proactively broadcasts the newly logged-in user's public key to all the online users. This way, the online users won't bother requesting its public key. This procedure is similar to the previous one, so we won't cover too much detail here.

Steps:
1. Right after authentication of a new client, server broadcasts its public key to all the other online authenticated users.
*New user command:* `COMMAND NEW_USER {username} {public_key} {signature} {checksum}`
2. Each client receives the public key and cache it if not existed yet.

### Communication Phase
Finally, after getting ready of all the session key and public keys, we can now start to chat.

We will use the following notations in the following paragraphs.
- `msg`: message
- `cipher`: any kind of encrypted data
- `||`: string concatenation
- `E`: symmetric encryption function
- `D`: symmetric decryption function
- `Sign`: digital signing function
- `SignV`: signature verifying function
- `MD5`: checksum hashing function (MD5 is used in our case)
- `MD5V`: checksum verifying function

#### 1. Client A Sending Message to Server
We notate the session key of the client A as `Ka` and private key as `Kpriv(a)`. Hence our encryption algorithm would be as follow:
```
sender_signature :=  Sign(Kpriv(a), msg)
encrypted := E(Ka, msg || sender_signature)
return encrypted || MD5(encrypted)
```
#### 2. Server Receiving Message from Client A
We notate server's private key as `Kpriv(S)`, the incoming message, i.e. the output of previous procedure, as `cipher`, and the public key of sender Client a as `Kpub(a)`. Hence the message parsing algorithm would be as follow:
```
md5_verified := MD5V(cipher)
if md5_verified:
  decrypted := D(Ka, cipher)
  sender_verified := SignV(Kpub(a), decrypted)
    if sender_verified:
    server_signature := Sign(Kpriv(S), decrypted)
    return decrypted || server_signature
```

> Note that `decrypted` is actually equal to `msg || sender_signature`, so the returned value is actually message with signatures, which is equal to `msg || sender_signature || server_signature`  

> Note also that in our implementation, server verifies both checksum for integrity and the signature of sender (client A) for authenticity, and it won't deliver the message if any of the verification fails. The sender verification may be redundant here, but after some considerations we decide to keep it to ensure a maximum level of security.  

#### 3. Server Delivering Message to Client B
Note that `msg_with_signatures` is the result of the previous procedure. We notate the session key of client B as `Kb`. Hence the server gets the deliverable message as follow:
```
encrypted := E(Kb, msg_with_signatures)
return encrypted || MD5(encrypted)
```

> Note that our chat system broadcasts messages to all online users, and the above procedure is only for getting deliverable message client B. Hence, the above procedure should be repeated for each receiver of the message.  

#### 4. Client B Receiving Client A's Message from Server
We notate the incoming data as `cipher`, public key of client A as `Kpub(a)`, and public key of server as `Kpub(S)`. And note that we use `remove_signatures` to remove signatures concatenated in the plain message to get the plain message itself. Hence, client B parses it as follow to get the plain text from client A:
```
md5_verified := MD5V(cipher)
if md5_verified:
  decrypted := D(Kb, cipher)
  // decrypted == msg || sender_signature || server_signature
  
  server_verified := SignV(Kpub(S), decrypted)
  sender_verified := SignV(Kpub(a), decrypted)
  if server_verified && sender_verified:
    return remove_signatures(decrypted)
```

### Group Chat
The group chat function mainly focuses on the relationship between groups and clients.

#### 1. Client wants to create a new chat group
We provide a function that let the client to add a new group, in case the new group's name is not exist. The client clicked the Create Group button in the ClientChatWindow, it will trigger the program to send `COMMAND ADD_GROUP` with client and new group's name. Server will receive the message and call GroupHandler to check whether the new group can be added or not. If it is valid, then return the group name to the client and added to the client's group list. If not, it will return is message `COMMAND FAIL_ADD`, and the message will be displayed on the chat window to notice the user.

#### 2. Client wants to add a member to group
It is also possible for the clients to add someone that is registered in the server to a group. It started from the client, the client can type a name and click ADD MEMBER button, it will trigger the system to send `COMMAND ADD_MEMBER` with new member name and group name. Server will receive the message and call GroupHandler to check whether  the new mamber can be added or not. If it is valid, then return a valid message to the client and send the group name to the newly added client. If not, it will return is message `COMMAND FAIL_ADD`, and the message will be displayed on the chat window to notice the user.

#### 3. Client gets the Group List
Once the user is login successfully, the server will send the group names where the client belongs to. The group will added to the group list of ClientChatWindow.

### Termination Phase
Server will automatically disconnects a client if it's been idle for 5 minutes.
This ensure security in the case that user leaves the office with the laptop open and client logged in.
This also prevents idle clients from taking and wasting network resources.

Connected clients (both authenticated or unauthenticated) can also input `.bye` to terminate.

Steps:
1. Client sends termination command `COMMAND QUIT` to the server.
2. Server responds termination response `RESPONSE QUIT` and disconnects the client.
3. Client disconnects and terminates.

> Note that a force termination could be triggered by closing the GUI

## Extra Security Policy
- User registration is suggested to be done within the intranet of the company or organization that uses this chatting system. As two factor authentication secret is sent to client without extra protection. Note that this is the only time this secret gets transmitted in the network, so we only need to guarantee that registration happens in a secured environment. 
- Database (Database Mock in our implementation) should be protected properly.
- No one is accessible to server's *Server Master Private Key* in its config file.
- It's recommended that client retrieve server's *Server Master Public Key* from a trusted channel instead of retrieving it from the server itself (server could be forged).
- As long as server passes the challenge check by client, server should be trusted, given that server's *Server Master Public Key* is properly distributed with integrity to the client.
 
