# Room Optimization

This is simple application which suppose to have optimization services related to room services.
As part of current requirement, it is implemented one service which give optimized value incomes according to configured number of rooms and premium room threshold.

# How to run app
It is builded as Spring boot application and can be started from command line or from IDE environments.
To run it from command line you can execute
   mvn spring-boot:run 

# How to use it
Main APIs implemented here are (more details check in API documentation):
* **/availableRooms/configure** which is used to configure used actual setup like number of premium rooms and number of economy class rooms and premium threshold used in calulaction
* **/availableRooms/revenue** is endpoint used to return calculated optimal values and room ussage

# API documentation
Exposed APIs and sample requests adn responses could be found on following swagger link after you run application:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
 
