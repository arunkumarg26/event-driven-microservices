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
4. Access the Axon console using url http://localhost:8025/#query

This should bring up axon server in the port 8025

### Note: 
1. Individual microservice will automatically connect to this Axon server using axon-spring-boot-starter dependency in the pom
2. Start the individual applications and you should see the below log in the terminal where we started axon server 


```
     _                     ____
    / \   __  _____  _ __ / ___|  ___ _ ____   _____ _ __
   / _ \  \ \/ / _ \| '_ \\___ \ / _ \ '__\ \ / / _ \ '__|
  / ___ \  >  < (_) | | | |___) |  __/ |   \ V /  __/ |
 /_/   \_\/_/\_\___/|_| |_|____/ \___|_|    \_/ \___|_|
 Standard Edition                        Powered by AxonIQ

version: 4.5.12
2022-07-10 21:02:41.120  INFO 10423 --- [           main] io.axoniq.axonserver.AxonServer          : Starting AxonServer using Java 11.0.11 on OM-C02G63CUMD6R with PID 10423 (/Users/14919/Documents/AxonServer-4.5.12/axonserver.jar started by 14919 in /Users/14919/Documents/AxonServer-4.5.12)
2022-07-10 21:02:41.124  INFO 10423 --- [           main] io.axoniq.axonserver.AxonServer          : No active profile set, falling back to 1 default profile: "default"
2022-07-10 21:02:44.113  INFO 10423 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8025 (http)
2022-07-10 21:02:44.285  INFO 10423 --- [           main] A.i.a.a.c.MessagingPlatformConfiguration : Configuration initialized with SSL DISABLED and access control DISABLED.
2022-07-10 21:02:46.578  INFO 10423 --- [           main] io.axoniq.axonserver.AxonServer          : Axon Server version 4.5.12
2022-07-10 21:02:49.131  WARN 10423 --- [           main] .s.s.UserDetailsServiceAutoConfiguration :

Using generated security password: c64d4b1c-69f8-4366-a625-f1992e3bd35a

This generated password is for development use only. Your security configuration must be updated before running your application in production.

2022-07-10 21:02:51.217  INFO 10423 --- [           main] io.axoniq.axonserver.grpc.Gateway        : Axon Server Gateway started on port: 8124 - no SSL
2022-07-10 21:02:51.230  INFO 10423 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8025 (http) with context path ''
2022-07-10 21:02:51.793  INFO 10423 --- [           main] io.axoniq.axonserver.AxonServer          : Started AxonServer in 11.411 seconds (JVM running for 12.073)
2022-07-10 21:13:39.098  INFO 10423 --- [grpc-executor-1] i.a.a.logging.TopologyEventsLogger       : Application connected: orders-service, clientId = 11418@OM-C02G63CUMD6R, clientStreamId = 11418@OM-C02G63CUMD6R.1c8d9efa-d9f1-4458-b43a-86aa982fe8ec, context = default
2022-07-10 21:13:50.976  INFO 10423 --- [grpc-executor-4] i.a.a.logging.TopologyEventsLogger       : Application connected: payments-service, clientId = 11429@OM-C02G63CUMD6R, clientStreamId = 11429@OM-C02G63CUMD6R.064dfac9-2d8e-45cf-9634-1555b86d6c92, context = default
2022-07-10 21:13:55.605  INFO 10423 --- [grpc-executor-2] i.a.a.logging.TopologyEventsLogger       : Application connected: products-service, clientId = 11436@OM-C02G63CUMD6R, clientStreamId = 11436@OM-C02G63CUMD6R.61f91e2b-0817-464f-a1c7-f03ac59c6e30, context = default
2022-07-10 21:14:00.728  INFO 10423 --- [grpc-executor-4] i.a.a.logging.TopologyEventsLogger       : Application connected: shipments-service, clientId = 11443@OM-C02G63CUMD6R, clientStreamId = 11443@OM-C02G63CUMD6R.80b1286d-d663-4961-a462-42b7a5aa3090, context = default
2022-07-10 21:14:03.668  INFO 10423 --- [grpc-executor-3] i.a.a.logging.TopologyEventsLogger       : Application connected: users-service, clientId = 11447@OM-C02G63CUMD6R, clientStreamId = 11447@OM-C02G63CUMD6R.e3cc8949-a7f7-4f0d-919c-033f594e81cf, context = default
```

## Accessing the embedded H2 database

1. This sample POC uses H2 database which can be access through http://localhost:port/h2-console 
2. Example: http://localhost:8081/h2-console/ // To access orders database
3. Look for the file name in the src/main/resources/application.properties for the JDBC properties
4. Example: JDBC url for orders is jdbc:h2:file:~/data/orderDB, username: sa, password is password
5. Select the desired table and run to see the output
6. Note: Everytime we start the server, the database will be wiped. If you want to retain the records, change the property spring.jpa.hibernate.ddl-auto =update