# E-Clinic

* Website for clinical center management.
* Some of the features:
  * User(patient) registration and email verification.
  * Patients can request/cancel an appointment/examination.
  * Clinic administrator can define examination types, price list, free appointment slots, approve user registrations, approve doctor/nurse vacation requests, reserve clinic rooms for appointments...
  * Doctor/Nurse can view their working schedule and appointments in a calendar.
  * Doctor can fill patients examination report and prescribe recipes  which nurse needs to verify.
  * Doctor can review and update patients medical record (height, weight, allergies, blood type, appointment reports )
  * Clinic administrator can register new doctors and nurses.
  * Clinic administrator can view business report.
    
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
public static String appUrl = "https://e-clinic-isamrs.herokuapp.com/"; // for deployment
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
