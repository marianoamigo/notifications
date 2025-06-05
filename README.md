# Java + Spring Backend Challenge

You have to develop a basic Notifications management system for authenticated users. 
The system has to allow every logged user to manage and send notifications by different channels. 

### Features 

- Create new Users 
- Get Users by their Id
- Update Users
- Create new Notifications
- Get Notifications by their Id
- Get a User's Notifications list. 
- Update Notifications 
- Delete Notifications

## Pre- Requisites

- Docker installed without SUDO Permission
- Docker compose installed without SUDO
- Ports free: 8080, 8082, 8083, 8084, 3306

# How to run the APP

```
code for run the app (missing)
```

# How to run the tests

```
code for run the tests (missing)

```

## Areas to improve

- Data should be moved from tests to an external file
- Generic method should be used to mock endpoints
- Error handling could be improve (I.E handle already existing user error)
- A Seed migration would be useful to have an already working app with data
- Deployment could be done

## Errors to be fixed 

- Docker is not running with tests.
- Code coverage is not counting the hole code in e2e tests
- Deployment (missing)

## Techs

- Java: 21
- Springboot: 3.4.5
- Hibernate
- MySQL: 8.3.0
- JUnit: 5

## Decisions made 

- Clean Architecture: To be able to handle further changes in the future in a proper way.
- Hibernate: Because 
- Docker: to make it portable
- E2E testing was done because is not always necessary to test every single part. That's why
if the controller provide the proper answer the has passed.

## Route

- : [!API Swagger] (http://localhost:8080/swagger/index.html#)

## Env vars should be defined 

To find an example of the values you can use .env.example
