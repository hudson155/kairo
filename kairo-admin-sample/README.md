# Admin Sample

A sample Kairo application that demonstrates the admin dashboard.
Use this as a reference for integrating `kairo-admin` and `kairo-kdocs` into your own application.

## Running

Start a PostgreSQL database, then run:

```shell
CONFIG=development \
POSTGRES_USERNAME=postgres \
POSTGRES_PASSWORD=password \
./gradlew :kairo-admin-sample:run
```

The admin dashboard is available at [http://localhost:8080/_admin](http://localhost:8080/_admin).

### With KDocs

To include generated API documentation in the dashboard:

```shell
./gradlew :kairo-admin-sample:packageKdocs :kairo-admin-sample:run
```

## What's included

This sample registers the following features:

- `DependencyInjectionFeature` (Koin)
- `RestFeature` with sample endpoints
- `SqlFeature` with PostgreSQL
- `HealthCheckFeature`
- `KdocsFeature` for serving generated API docs
- `AdminDashboardFeature` with server name, docs URL, and API docs URL configured

It also includes two small domain features (`AuthorFeature` and `LibraryBookFeature`)
to demonstrate how endpoints and dependencies appear in the dashboard.
