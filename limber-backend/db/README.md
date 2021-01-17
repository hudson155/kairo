# Databases

Sharing code (e.g. database migrations) between modules
means that when a database is shared between multiple modules,
those modules can keep a consistent view of the database.

For SQL databases, this means DDL to keep the schema in-sync.
