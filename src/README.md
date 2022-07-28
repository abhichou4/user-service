# User Web Service

A RestAPI service that lets you add users to MySQL database.

# Pre-requisites
* Java 1.8
* Maven
* MySQL

# How to run the code

Before running, create an empty db in MySQL where the users will be added.
```bash
sudo mysql --password
mysql> create database users; -- Creates the new database
mysql> create user 'abhichou4'@'%' identified by 'abcd1234';
mysql> grant all on db_example.* to 'sample_username'@'%';
```

Schema will be created automatically when you first run the app!

If you went with some other username/password, go ahead and update it in `resources/application.properties`
```yaml
spring.datasource.username=your_username
spring.datasource.password=your_password
```

You can either run `UserServiceApplication` or create an artifact and run that.

Create a jar artifact
```bash
mvn clean package
```
Execute the artifact and run the server!
```bash
java -jar target/user-service-0.0.1-SNAPSHOT.jar
```

Now the server is running at `localhost:8080`

# Test the service!

### Add new users

```bash
curl --location --request POST 'localhost:8080/user/create' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "Abhineet Choudhary",
"email": "abhichou4@gmail.com",
"addressPinCode": "400101"
}'
```

Keep the received Id!

### Fetch the user

```bash
curl --location -g --request GET 'localhost:8080/user/{id_from_prev_request}'
```

### Try an invalid email

```bash
curl --location --request POST 'localhost:8080/user/create' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Abhineet Choudhary",
    "email": "abhichou4@invalidemail",
    "addressPinCode": "400101"
}'
```