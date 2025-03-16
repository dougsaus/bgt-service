# bgt-service

This project is a graphql service for tracking information about board games and user board game collections.

This service will aggregate data for a board game stored locally by this service with enrichment from the Board Game Geek (BGG) REST api.  

Future versions will support tracking user collections and integrate with user existing collections on BGG.



## Running Tests

The tests in this project use testcontainers so ensure you have a local docker daemon running and the tests should run out-of-the-box.
```bash
./gradlew clean test
```

## Running the Application Locally
Running this application locally expects a postgres DB to be running on port 5432 with a DB named 'bgt'.  You can spin one up in docker as follows:
```bash
docker run -d --name bgt -p 5432:5432 -e ALLOW_EMPTY_PASSWORD=yes -e POSTGRESQL_DATABASE=bgt bitnami/postgresql:latest
```

The easiest way to run the app is from the command line.  The local profile will default to port 8080.  You can change the profile's `server.port` to 0 if you prefer a random available port.
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

The GraphQL playground will be available on: http://localhost:8080/graphiql

If you'd like to seed the database with some data from BGG, I have included a CSV containing the top 1000 ranked board games from BGG.  You can seed the DB by running the following GraphQL mutation from graphiql.  Just replace the value of project root with the path to you local repo.

```graphql
mutation seed {
  seedGames(filename: "<project root>/boardgames-top1000.csv") {
    id
    name
  }
}
```

## Querying Games

You can query games with a query like this:
```graphql
query {
    queryGames {
        games {
            id
            bggId
            name
        }
    }
}
```

When 'metadata' is requested, the metadata is actually aggregated from the BGG REST Api and returned in this single graphql api:
```graphql
query {
    queryGames {
        games {
            id
            bggId
            name
            metadata {
                description
            }
        }
    }
}
```

## Creating Games

You can create games with the following mutation.  The id of the bgg game is optional, but required if you want to enrich metadata.
```graphql
mutation {
    createGame(input: {bggId: 1234 name: "My new game"}) {
        id
        bggId
        name
    }
}
```