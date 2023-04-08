
# 🍺 🌐 __BEER CATALOGUE__ 📝 🍻
## A simple access-controlled service to report and retrieve beers and beer manufacturers information stored in a relational database. Project to showcase the use of Java, PostgreSQL and Spring.
___

![GitHub contributors](https://img.shields.io/github/contributors/DRBondyaleJuez/BeerCatalogue)
![GitHub repo size](https://img.shields.io/github/repo-size/DRBondyaleJuez/BeerCatalogue)
___

## __DESCRIPTION__
This code was used as a training exercise to practice coding in java, postgreSQL and spring.

To develop this simple service this code uses [Spring Boot](https://spring.io/projects/spring-boot). It has only 3 endpoints 1 get and 2 posts.
The persistence uses relational database storage based on [PostgreSQL](https://www.postgresql.org/). The relational database stored the beer, manufacturer and access control information.

The service allows any client to retrieve information about the beers and manufacturer provided by the service.
It first consults the information present in the database and in the case of beers it also consults another API for information
if it is absent from the database and then returns the information demanded if possible.

Clients can also create a user profile associated with a particular manufacturer. Then this user after signing in they can 
add or modify beer or manufacturer information associated with the manufacturer their profile has authorization to edit.

There is also an admin profile which is able to perform any operation on any entry in the database after signing in.

<div style="text-align: center;">

![Diagram](https://user-images.githubusercontent.com/98281752/230742525-695752b8-767a-439d-86a2-218fb7c61adb.png)

</div>

___
___



## __PERSISTENCE & RELATIONAL DATABASE__

This services storage is based on a relational database with interactions with it coded in this case in postgreSQL.

The relational database follows the following self-explanatory diagram:

<div style="text-align: center;">

![DB diagram](https://user-images.githubusercontent.com/98281752/230742527-32cf88b4-16ad-479f-af25-4dfdcd4da7ac.png)

</div>


___
___

## __USAGE__
This is a simple service designed based on exercise to train and showcase java, postgreSQL and spring capabilities. The service has the following endpoints:

### _EndPoints:_
<!-- OL -->
- #### To create a user profile and sign in
    - #### _/users_
      > __PUT__

      > __POST__

- #### To add, retrieve and edit beer information
  - #### _/allbeers_
    >   __GET__ 

  - #### _/beers_

    > __GET__

    > __POST__

    > __PATCH__

- #### To add, retrieve and edit manufacturer information
    - #### _/allmanufacturers_
      >   __GET__

    - #### _/manufacturers_

      > __GET__

      > __POST__

      > __PATCH__

- #### To view API documentation
  - #### _/swagger-ui/index.html_
    > __GET__

For further comprehension of the service you can consult this [API Documentation](https://app.swaggerhub.com/apis-docs/DANFL4_1/BeerCatalogue/1.0.0) formatted using [Swagger](https://swagger.io/) editor.

<div style="text-align: center;">

[![APIDoc image](https://user-images.githubusercontent.com/98281752/230742526-623b2f4b-1dc9-4467-b736-b35949712097.png)](https://app.swaggerhub.com/apis-docs/DANFL4_1/BeerCatalogue/1.0.0)

</div>


___
___

## __INSTALLATION INSTRUCTIONS__
### __For IDE:__
<!-- OL -->
1. Clone the repository in your local server
2. Run the project's Main Class in your IDE

### __For Ubuntu (In terminal):__
<!-- OL -->
1. If necessary [install java version 11 or higher](https://stackoverflow.com/questions/52504825/how-to-install-jdk-11-under-ubuntu)

    ```bash
        sudo apt-get install openjdk-11-jdk
    ```


2. If necessary [install maven version 3.6.3 or higher](https://phoenixnap.com/kb/install-maven-on-ubuntu)

   ```bash 
       sudo apt install maven
   ``` 

3. If necessary [install git](https://www.digitalocean.com/community/tutorials/how-to-install-git-on-ubuntu-20-04)

   ```bash 
       apt install git
   ```

4. Clone the repository

   ```bash 
       git clone https://github.com/DRBondyaleJuez/BeerCatalogue.git
   ```

5. Go to the project folder. Make sure the [pom.xml](https://maven.apache.org/guides/introduction/introduction-to-the-pom.html) is there.

6. Create [.jar file](https://en.wikipedia.org/wiki/JAR_(file_format)) executable in target folder using the following code:

    ```bash
        mvn install 
    ```

7. Build the database and tables needed for this application's persistence. Follow the recommendations in the resources.RelationalDatabaseSchema which describes the code in PostgreSQL.
   ([Help creating your first database in pgAdmin](https://www.tutorialsteacher.com/postgresql/create-database))


8. Fill in the parameters needed in the secrets.properties for access to the PostgreSQL database and for the encryption


9. This code uses javafxml so we recommend the use of the following code  to run the program :

   ([*Source*](https://github.com/openjfx/javafx-maven-plugin))

   ```bash 
       mvn javafx:run
   ```



___
___
## __INSTRUCTIONS FOR CONTRIBUTORS__
The objective of the project was to practice and apply java knowledge. No further contributions will be needed all of this is just a training excercise.

Hope you may find the code useful and please acknowledge its origin and authorship if used for any other purpose.

















