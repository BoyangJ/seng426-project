# saturn

## Development

#### Required Software

1. Java 8 or greater
2. NPM 2.13 or greater
3. Maven 3.3 or greater
4. MySQL 5.5 or greater

#### Configuration

To configure the system:

1. Run `npm install` from inside the application's folder
2. Create database schema for the application with the name of your preference (the tables are created and populated automatically during the first run)
3. Open the configuration file `config/application-dev.yml` and set:
   1. `spring.datasource.url` to the database host, port and schema (defaults to **localhost**, **3306** and **saturn** respectively).
   2. `spring.datasource.username` to the database username (defaults to **saturn**)
   3. `spring.datasource.password` to the password of the database user set on previous step (defaults to **saturn**)
   4. `server.port` with server port (defaults to **8080**)

After that execute `mvn spring-boot:run` from _inside_ the application's folder to run the application and navigate to [http://localhost:8080](http://localhost:8080) in your browser (note the port number might be different according to your configuration and sudo might be necessary if the application is configured to run on a port below 1024).

## Distribution

#### Building package

To build a distribution package:

1. Run `mvn clean package`
2. Copy `target/saturn-1.0.4.war` file to a new folder
3. Inside this folder create two subfolders, one called `config` and one called `mediaResources`
4. Copy `src/main/resources/config/application-dev.yml` into the `config` folder

#### Running package

To run the distribution packaged application:

1. Create database schema for the application with the name of your preference (the tables are created and populated automatically during the first run)
2. Configure the database and server port in the `config/application-dev.yml` file as explained above
3. Run `java -jar saturn-1.0.4.war` (sudo might be necessary if the application is configured to run on a port below 1024)

Finally navigate to [http://localhost:8080](http://localhost:8080) in your browser (note the port number might be different according to your configuration and deployment environment).
