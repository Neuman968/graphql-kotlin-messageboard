# Ktor GraphQL Kotlin 

This is a demo application to showcase how to implement a GraphQL api with kotlin and Ktor. 

## Starting the database. 

using docker, the database can be created with the following command.

```bash
docker run --name messageboard-db -p "5432:5432" -e "POSTGRES_PASSWORD=postgres" -v "pg-init:/docker-entrypoint-initdb.d" postgres
```

The application can be run on port 8080 with the command.

```bash
./gradlew run
```
