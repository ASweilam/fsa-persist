# fsa-persist
Microservice that consumes FSA API and persist into database. Built with Spring Boot and Hibernate.

## What is the purpose?
This microservice consumes an API from Food Standards Agency (FSA) and does a number of processing and re-mapping of
the json and stores the data into a MySql database. 

This can be helpful when the data needed to be stored to increase availability and enables creating database level
operations such as stored procedures. 

In this case, the data was re-mapped and stored to serve as a datasource for another API. 

## Endpoint consumed

- For this project, a specific FSA endpoint is consumed:
```bash
https://ratings.food.gov.uk/OpenDataFiles/FHRS774en-GB.json
```

## Database Schema

Database schema file is stored under:
```bash
dbScript\create-db.sql
``` 

The database (fsa) contains two tables:

- establishment_details: containing the establishment details data and a foreign key to local_authority table.
- local_authority: containing the local authorities associated with establishment details.
- Both tables are in a one-to-many relationship  


## Installation

- Clone the project.

```bash
$ git clone https://github.com/ASweilam/fsa-persist.git
```

- The project uses MySql as a datasource. Download and install [MySql](https://dev.mysql.com/downloads/installer/)
and [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)

- Inside the dbScript directory from the project's root there is a create-db script that will create the database for the project. 

- Go to application.properties file inside the project to change the database username and password to correspond to your setup of the database.

- Make sure to install the Maven dependencies before running the application
```bash
mvn clean install
```

## Components

| Package  | Class | Description
| ------------- | ------------- | ---------
| `gateway`  | EstablishmentDetailFromExternal  | Gets a generic response from an external API
| `model`  | EstablishmentDetail  | A model for establishment details
| `model`  | LocalAuthority  | A model for local authority
| `service`  | EstablishmentDetailService  | Service class that process the incoming response, re-map it for Establishment Detail Model and persist into database
| `service`  | ELocalAuthorityService  | Service class that process the incoming response, re-map it for Local Authority and persist into database



## Contributing
Pull requests are welcome.


## License
[MIT](https://choosealicense.com/licenses/mit/)
