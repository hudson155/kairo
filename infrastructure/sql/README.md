# SQL Infrastructure

Limber
has [1 Postgres database](https://console.cloud.google.com/sql/instances/limber/overview?project=limberio)
on GCP. There's nothing particularly interesting about its configuration. It's not managed by
infrastructure-as-code, it's just managed manually through the GCP UI.

## User management

The way user management works is pretty cool. GCP's SQL offering defines 2 types of users:
PostgreSQL users and IAM users.

### PostgreSQL users

These are typical users like you'd expect with a Postgres database.

- `postgres` is the default user. It's not used for anything. The password isn't even documented
  anywhere.
- `limber` is a custom created user used by Limber's backend. The password also isn't documented
  anywhere - it's only stored as a Kubernetes secret.

### IAM users

These are the interesting ones. Any GCP IAM user can be given access to the Postgres instance.
Instead of being given a username and password, however, they log in to the database using their GCP
email address and a temporary token.

If you're trying to do this locally, make sure you're on a network that's approved by the SQL
instance.

```
PGPASSWORD=$(gcloud auth print-access-token) psql \
    --host=<IP ADDRESS> \
    --username=<EMAIL_ADDRESS> \
    --dbname=limber
```

## Migrations

### DML migrations

DML database migrations are managed using Flyway.
See [the db folder](/limber-backend/db/) which has a module for each database.

### Roles and privileges

Roles and privileges are managed manually.
When new schemas are created, privileges should be granted appropriately.

#### Roles

- `cloudsqliamuser` was created automatically by GCP.
    It provides no explicit permissions.
    Users are added automatically by GCP.

- `limber_user_readonly` was created manually.
    It's used for users that need read access.
    ```postgresql
    CREATE ROLE limber_user_readonly;
    ```
    Users can be granted this role with
    ```postgresql
    GRANT limber_user_readonly TO "<EMAIL_ADDRESS>";
    ```

- `limber_user_ddl` was created manually.
    It's used for users that need DDL write access.
    ```postgresql
    CREATE ROLE limber_user_ddl IN ROLE limber_user_readonly;
    ```
    Users can be granted this role with
    ```postgresql
    GRANT limber_user_ddl TO "<EMAIL_ADDRESS>";
    ```

#### Schemas

When new schemas are created, you should do the following:

```postgresql
GRANT USAGE ON SCHEMA <SCHEMA_NAME> to limber_user_readonly;

ALTER DEFAULT PRIVILEGES IN SCHEMA <SCHEMA_NAME>
    GRANT SELECT ON TABLES TO limber_user_readonly;
GRANT SELECT ON ALL TABLES IN SCHEMA <SCHEMA_NAME> TO limber_user_readonly;

ALTER DEFAULT PRIVILEGES IN SCHEMA <SCHEMA_NAME>
    GRANT INSERT, UPDATE, DELETE ON TABLES TO limber_user_ddl;
GRANT INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA <SCHEMA_NAME> TO limber_user_ddl;
```

This was also done for the `public` schema.
