# Permissions

Limber has 3 layers of permissions.

From coarsest-grained to finest-grained, the permission layers are:

1. **Limber permissions:**
    These are the most basic form of permissions.
    They define how users may interact with Limber as a whole,
    outside the context of any org or feature.
    Typical users will not have any Limber permissions at all.
    Some examples of Limber permissions include:

    - `IDENTITY_PROVIDER`: Used for Auth0.
    - `SUPERUSER`: Allows internal users to access all orgs.

    Limber permissions are defined in this library.

1. **Org permissions:**
    These are permissions that exist at the org level,
    defining how users may interact with the org as a whole.
    Many of these have to do with org management activities,
    rather than with functional use of the features of Limber.
    Each org has the same set of permissions.

    Org permissions are defined in this library.

1. **Feature permissions:**
    These are permissions that exist at the feature level,
    defining how users may interact with that feature.
    Each feature of the same type has the same set of permissions,
    but features of different types will have different sets of permissions.

    Feature permissions are defined in each module,
    using base classes from this library.

Looking at the permission enum classes will provide more color.
    
### Type conversion

This library also provides type converters,
which can be used for serialization
(using the [serialization library](/limber-backend/common/module) or for the database).
Register the type converters to do so.

Note: Deserializing a `FeaturePermissions` string may yield `UnknownFeaturePermissions`.
This happens when the permissions are for
a feature with a prefix unknown to the deserializer.
