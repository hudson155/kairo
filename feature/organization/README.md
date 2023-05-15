# Organization Feature

As a multi-tenant application,
Limber uses the Organization Feature to store tenant-specific information.

This includes basic tenant metadata and the list of features for that tenant,
but should not include anything pertaining to feature implementations or users.

## Organizations

- An **organization** is the representation of a tenant within Limber.
- An **organization auth** is the representation of the auth-related information for an organization.
  This associates it with Auth0.
  Organization auths are not created automatically when organizations are created.
  They need to be created manually.
- An **organization hostname** helps the frontend identify which organization to use depending on the hostname.

## Organization features

A **feature** represents a component of an individual organization's Limber implementation.
Organization features are backed by Limber features.
For example, `Form` features are backed by the `form` feature.

**A note about organization features:**
An organization's features are different from Limber features.
Limber Features are a building block in the Limber framework.
Organization features define a piece of functionality in the app.
However, organization features are backed by Limber Features.
