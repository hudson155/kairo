# SQL Infrastructure

Limber
has [1 Postgres database](https://console.cloud.google.com/sql/instances/limber/overview?project=limberio)
on GCP. There's nothing particularly interesting about the configuration of it. It's not managed by
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
