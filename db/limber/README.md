# Limber database

This is currently the only database.
It's shared across the entirety of Limber.
The migration files herein are separated into 3 folders.

- **`common`:** Contains DDL migrations that should run in all environments.
- **`fixtures`:** Contains DML fixture migrations that should only be used in development environments.
- **`gcp`:** Contains GCP-specific DCL migrations that should only be used on Google Cloud Platform.
