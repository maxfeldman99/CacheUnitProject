# CacheUnitProject

Java project , Part 2,3 - MMU - Cache Unit and Server implementation\

This part of the MMU project includes the server interface that is running in a multi threaded envioremnt, each client sends a request to store data inside the server cache unit.\
the data is beeing stored using Paging algorithms that were inclued with a JAR file.\
after each request the user can ask for information about the sroting process using GUI.\
each request is made in a synchroized way to avoid a race condition scenario.\
each storing/reading action is tested using Junit5.\

![CLIENT GUI DEMO](http://i65.tinypic.com/2b1ikn.jpg)
