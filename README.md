# Weatherman

## Description

This Micro Service uses NASA API to retrieve SOLs Average Temperature. Is single endpoint API. You can get last seven SOLs Average Temperature that contains in NASA System.

## Flux

You can have three possibilities calling this API: Give SOL and receive this SOL´s Average Temperature, give SOL, but this not contains on Available NASA API list and request without any SOL and get all SOL´s Averages Temperature that are available

1. Getting all SOL´s Averages Temperature (Example)
Request: http://localhost:8080/nasa/temperature
Response: 

[
  {
    "sol": "421",
    "averageTemperature": -56.533
  },
  {
    "sol": "422",
    "averageTemperature": -50.919
  },
  {
    "sol": "423",
    "averageTemperature": -64.193
  },
  {
    "sol": "424",
    "averageTemperature": -57.473
  },
  {
    "sol": "425",
    "averageTemperature": -59.612
  },
  {
    "sol": "426",
    "averageTemperature": -66.168
  },
  {
    "sol": "427",
    "averageTemperature": -60.647
  }
]

2. Getting specific SOL Average Temperature (Example)
Request: http://localhost:8080/nasa/temperature?SOL=421
Response: 

[
  {
    "sol": "421",
    "averageTemperature": -56.533
  }
]

3. Getting specific SOL Average Temperature, but doesn´t exist (Example)
Request: http://localhost:8080/nasa/temperature?SOL=420
Response: 

HttpStatus: 451 / Message: This SOL is not available more

## How to make it work

* First, you need setup Java 8 Environment (JDK, JRE... )
* After, open this project in any IDE (i recommend Intellij)
* Before running this API on IDE, you need to run eureka-preoday and config-preoday (Observation: without this service, weatherman don´t will up, it will complain about missing properties)
* Ensure that you have Lombok plugin on IDE, as well configuration to put Lombok Annotation Processor
* Now, you can run without problems :)

## Architecture

Principal resource and unique is a SOL

1. Uses DTO object to receive and tranfer JSONs between controller
2. Controller is responsible for communication between frontend and backend.
3. Service is responsible for business logic, that makes all fluxs
4. NasaRestTemplate is responsible for send requests to NASA API
5. Extractor is responsible to tranform generic Java object to custom object

This Micro Service has communication with Eureka, that is registered to it, and Config Server, that information about API-KEY, API-VER and API-FEED-TYPE is gotten
