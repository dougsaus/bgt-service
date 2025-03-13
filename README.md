# bgt-service

This project is a service for tracking information about board games and user board game collections.

Running this application locally expects a postgres DB to be running on port 5432 with a DB named 'bgt'.  You can spin one up in docker as follows:
```
docker run -d --name bgt -p 5432:5432 -e ALLOW_EMPTY_PASSWORD=yes -e POSTGRESQL_DATABASE=bgt bitnami/postgresql:latest
```