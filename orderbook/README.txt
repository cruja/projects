
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

==== How to start everything ==== 
First, open the web server in NetBeans and click Run.
Second, open the matching server in IntellijIdea and configure AppRunner main class with <el ec 10 ""> parameters.

==== Tips ==== 
In the browser, blue boxes are buy orders and red boxes are sell orders.
As a rule of thumb, you have to add and place an order to make it available 
in the matching server/gui. Placing a range order will automatically do it for you.


