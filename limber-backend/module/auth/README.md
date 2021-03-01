# Auth Module

Authentication is performed by Auth0, so the auth module is not actually responsible for
authenticating users. It's also not really responsible for authorization either, since authorization
is handled on a per-endpoint basis regardless of which module it's in. What does this auth module
actually do, you might ask? Well, since I've already told you what it doesn't do, here's what it
does do.

### Communication with Auth0

The auth module is responsible for communication with Auth0. This could mean a few different things.
The primary use case right now is that when users authenticate with Auth0, Auth0 needs to get enough
information about them to create a JWT for them. Auth0 has some of this information, but reaches out
to the auth module for the rest.

### Roles

The auth module is responsible for **org roles** and memberships. Within an org, users can be
members of org roles. Org roles grant permissions within an org.

The auth module is responsible for **feature roles** and memberships. Org roles can be granted
feature roles, allowing users with membership in the org role to have feature permissions as well.

The org owner implicitly has permission to manage roles.
