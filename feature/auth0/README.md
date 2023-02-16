# Auth0 Feature

The Auth0 Feature enables interaction with Auth0
through their [Management API](https://auth0.com/docs/api/management/v2).

The `Auth0ManagementApi` class that is exposed by this feature is a facade,
which can be configured to either delegate to the real API
or to use a fake.
