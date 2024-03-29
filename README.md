
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

Clients can also create a user profile associated with a particular manufacturer. This user, after signing in, can 
add or modify beer or manufacturer information associated with their manufacturer's profile authorization.

There is also an admin profile which is able to perform any operation on any entry in the database after signing in.

<div style="text-align: center;">

![Diagram2](https://user-images.githubusercontent.com/98281752/231198775-8153727d-0eb1-428e-be7d-6a8a14f66486.png)

</div>

___
___



## __PERSISTENCE & RELATIONAL DATABASE__

This services storage is based on a relational database with interactions with it coded in this case in [PostgreSQL](https://www.postgresql.org/).

The relational database follows the following self-explanatory diagram designed using [drawSQL](https://drawsql.app/teams/danrbj/diagrams/beercatalogue):

<div style="text-align: center;">

[![DB diagram](https://user-images.githubusercontent.com/98281752/232341796-5a018d29-510e-4bac-b3f0-ae1773d52b70.png)](https://drawsql.app/teams/danrbj/diagrams/beercatalogue)

</div>

The primary key of the authorizations' table is the combination of the username and the manufacturer_name value.
Similarly the primary key of the beers' table is a combination of the beer_name and manufacturer_name.

___
___

## __USAGE__
This is a simple service designed based on exercise to train and showcase java, postgreSQL and spring capabilities. The service has the following endpoints:

### _API EndPoints:_
<!-- OL -->
- #### To create user profile, log in or create an admin profile
    - #### _/users/sign_in_
      > __PUT__
    - #### _/users/log_in_
      > __POST__
    - #### _/users/admin_
      > __PUT__

- #### To add, retrieve and edit beer information
  - #### _/allbeers_
    >   __GET__ 

  - #### _/beers_

    > __GET__

    > __PUT__

    > __PATCH__

- #### To add, retrieve and edit manufacturer information
    - #### _/allmanufacturers_
      >   __GET__

    - #### _/manufacturers_

      > __GET__

      > __PUT__

      > __PATCH__

- #### To view API documentation
  - #### _/swagger-ui/index.html_
    > __GET__

For further comprehension of the service you can consult this [API Documentation](https://app.swaggerhub.com/apis-docs/DANFL4_1/BeerCatalogue/1.0.0) formatted using [Swagger](https://swagger.io/) editor.

<div style="text-align: center;">

[![APIDoc image](https://user-images.githubusercontent.com/98281752/232342443-37b3649f-c44d-4b2d-8278-9b1fd09235cf.png)](https://app.swaggerhub.com/apis-docs/DANFL4_1/BeerCatalogue/1.0.0)

</div>

### _PERMISSIONS & AUTHORIZATIONS_

The use of some endpoints is not possible without previous authentication and some have further particular restrictions.

A new user could get beer and manufacturer information without requiring the creation of a user profile.

But a user profile is needed for other endpoints. After using the sign in endpoint a user can log in using the corresponding endpoint.
Once correctly logged in, a standard user is provided an authentication token, this is a UUID required to include in the body of the authorization restricted endpoints. 
The first of these operations is to add a new manufacturer information entry in the database. Then this particular user will have authorization to update the information of that particular manufacturer and also to 
add and update beers related to that particular manufacturer. However, this user won't be able to add or modify other manufacturers or beer that have other manufacturers.

There is a special kind of user (admin user) which can add or update any beer or manufacturer. Admin users need to also log in to be able to perform this operations.
However, only admins can create other admin user profiles. For this reason we recommend the creation of an original admin user during the creation of the database tables.
This is described in the RelationalDatabaseInitialDesignInstructions file in resources.

The following table is a summary of the permissions described:

![Permissions table](https://user-images.githubusercontent.com/98281752/232091614-8ee22145-7744-4dfd-b53f-28770ac34f6f.png)

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
   in case tests are giving trouble, this can be used to install without tests. However, it is not recommended.
   ```bash
        mvn package -Dmaven.test.skip
    ```

7. Build the database and tables needed for this application's persistence. Follow the recommendations in the resources.RelationalDatabaseSchema which describes the code in PostgreSQL.
   ([Help creating your first database in pgAdmin](https://www.tutorialsteacher.com/postgresql/create-database))


8. Fill in the parameters needed in the secrets.properties for access to the PostgreSQL database and for the encryption
    
    These are:
    <!-- OL -->
    - <ins>*DBUser*</ins>  (String username of the database software)
    - <ins>*DBPassword*</ins>  (String password of the database)
    - <ins>*encryptionKey*</ins>  (String a key for the encryption)
    - <ins>*saltSize*</ins>  (int the size of the desired salt)
    - <ins>*initialSubstringPositionForTransposition*</ins>  (int related to the transposition during the encryption. It should __not__ be 0, 1 or larger than half the encryption key size)


9. Once the target folder is created check it contains a jar file with the name of the project. Inside the target folder execute the following code to run the program :

   ([*Source*](https://askubuntu.com/questions/101746/how-can-i-execute-a-jar-file-from-the-terminal))

   ```bash 
       java -jar BeerCatalogue-1.0-SNAPSHOT.jar
   ```

___
___
## __INSTRUCTIONS FOR CONTRIBUTORS__
The objective of the project was to practice and apply java knowledge. No further contributions will be needed all of this is just a training excercise.

Hope you may find the code useful and please acknowledge its origin and authorship if used for any other purpose.

















