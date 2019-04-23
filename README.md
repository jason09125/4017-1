## User manual

### Log in from client

After connecting to the server, input `.login {username} {password} {token}` to log in.

> token is a time-based one time password (OTP), you should retrieve it from an app like Google Authenticator

### Quit the program from a client

Anytime use `Ctrl + C` or input `.bye` to quit.

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
1. Run server on a port, say 6000: `java -cp JavaChat-1.0.jar server.chat.ChatServer 6000`

> Or use `mvn exec:java -Dexec.mainClass="server.chat.ChatServer" -Dexec.args="6000"` to run directly with Maven in root directory

#### Running client

1. Make sure server is already up, say at `localhost` and on port `6000`
1. Get your client config file ready, say at the same path as the JAR package named `eric.properties`
1. Go to the directory where you have the JAR package ready
1. Run: `java -cp JavaChat-1.0.jar client.chat.ChatClient localhost 6000 eric.properties`

> Or use `mvn exec:java -Dexec.mainClass="client.chat.ChatClient" -Dexec.args="localhost 6000 ./client-config/eric.properties"` to run directly with Maven in root directory

#### Terminating

For both client and server programs, input `Ctrl + C` in the terminal to terminate it

#### Side note: creating a new user

1. Follow the steps for building (or use the prepared Jar package for you)
1. A key pair generator for registration is ready for you, run this: `java -cp ./target/JavaChat-1.0.jar client.auth.RegistrationHelper`
1. Save the public key and private key (perhaps save it in a .properties file, as you will use it to login later)
1. Run `java -cp ./target/JavaChat-1.0.jar server.user.UserManager {username} {password} {public-key}`
1. Retrieve the *two-factor authentication secret* (something looks like `A2PUWWNZZ43Y7MON`) and put it in a time-based OTP authenticator (e.g. Google Authenticator)  

> Note that we specifically require generating key pair on client side (in client package) and registering on server side (in server package) for security reasons.

> Note that you can use any key pair generator, as long as it's a RSA key pair. We just provide one generator for you for your convenience, it does not mean that you must use it.
