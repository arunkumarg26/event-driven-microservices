# event-driven-microservices
event-driven-microservices using Axon Framework

## Download Axon Server

1. Go to https://developer.axoniq.io/download 
2. Choose Axon Server JAR variant (alternatively you could choose docker version too)
3. Choose the latest version and clinic download
4. Unzip the folder


## Setting up configs in Axon Server
1. Create a folder inside the downloaded Axon server called Config (If doesn't exist)
2. Create a file named axonserver.properties and add the following properties in 

    ```
    server.port=8025 // Port used by Axon
    axoniq.axonserver.name=Aruns Axon server //Could be anything
    axoniq.axonserver.hostname=localhost
    axoniq.axonserver.devmode.enabled=true // We need this to enable developer options like resetting event store etc
   ```

## Starting the Axon Server
1. Navigate to the folder where the Axon Server is downloaded via terminal or cmd prompt
2. cd AxonServer-4.5.12
3. java -jar axonserver.jar

This should bring up axon server in the port 8025

### Note: 
1. Individual microservice will automatically connect to this Axon server using axon-spring-boot-starter dependency in the pom
2. Start the individual applications and you should see the below log in the terminal where we started axon server 



![alt text](../../../../../../../var/folders/56/1rzl77bj36v317n5r60t9mkw0000gp/T/TemporaryItems/NSIRD_screencaptureui_ddjv01/Screen Shot 2022-07-11 at 3.25.24 PM.png)