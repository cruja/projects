
==== Order matching project ====

=== Technologies/frameworks used ===
Java 8,
NetBeans 8.0.2 / Glassfish 4.1,
IntellijIdea 14.0.3
Maven 3

Web related technologies used: JavaScript, JQuery, HTML5/WebSockets.

The communication between the web server and the order matching server is done in an async manner.

==== Project folder structure ==== 
The OrderBookWebServer folder contains a NetBeans project, the web server.
The OrderbookApp folder contains an IntellijIdea project, the order matching server & desktop gui.
The common folder contains sources used in common by all other components.

=== How to build it ===
Two modules are under maven control: common and OrderbookApp.
In order to build and install them in your local maven repository, 
you'll have to run "mvn clean install" under orderbook folder.

==== How to start everything ==== 

You will first have to start the web server and only then the matching server.

1. How to start the web server:
Open the web server project, OrderBookWebServer, in NetBeans IDE
Add the common fat artifact, generated during build step, within the NetBeans project's Libraries folder.
You may also have to add the cdi-api.jar under the same folder. It contains javax apis.

2. How to start the order matching server:
2.1 Import the order matching server, OrderbookApp, in IntellijIdea IDE as a maven project.
    Add the common fat artifact as a project library dependency.
    Configure org.trading.orderbook.MatchingServer as the main class. 
    Click Run.
2.2 Due to the fact that this maven module has been generated as a self contained executable jar archive,
    you could just run the command: "java -jar orderbook-1.0-SNAPSHOT-fat.jar".
Note: you will notice a orderbook.log file under the folder you're running the matching server from.

==== Tips ==== 
In the browser, blue boxes are buy orders and red boxes are sell orders.
As a rule of thumb, you have to add and place an order to make it available 
in the matching server/gui. Placing a range order will automatically do it for you.


