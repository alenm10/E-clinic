# eClinic
[![Build Status](https://travis-ci.com/ISAMRST07/MRSISA2020_T07.svg?branch=databaseDev)](https://travis-ci.com/ISAMRST07/MRSISA2020_T07)
## Technologies used

* MySQL
* Spring boot
* Vue.js

## Deployment

The application is deployed [here](https://e-clinic-isamrs.herokuapp.com/ "eClinic") using [Heroku](https://heroku.com "Heroku").

## Installation

There are two possible ways of running the application

### Without the maven build

**Warning:** Delete every file from *backend/src/main/resources* folder.

**Note:** You should comment the line 

```
public static String appUrl = "https://e-clinic-isamrs.herokuapp.com/"; // za deployment
```

and uncomment this one:

```
//    public static String appUrl = "http://localhost"; // za development
```

Start backend and the frontend.

*The application should be available at the default frontend url: http://localhost:8080*.


### Installation using Maven

The recommended way of installaation is by running:

```
mvn clean install
mvn --projects backend spring-boot:run
```

## Team members

* [Gegić Haris](https://github.com/gegic "gegic")
* [Mujo Alen](https://github.com/alen1010 "alen1010")
* [Manojlović Damjan](https://github.com/Damjan032 "Damjan032")
