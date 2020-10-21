# `limber-backend:common:permissions`

Permissions for the entire app (including all application modules)
are defined here. This includes:
* The `AccountRole` enum, which is the most basic form of permissions.
    Roles assigned to an account grant that account broad app-level permissions.
    For example, `IDENTITY_PROVIDER` is granted to the Auth0 account,
    and `SUPERUSER` is granted to internal accounts with highly privileged access.
    Users in customer orgs should not have `AccountRole`s.
* `OrgPermissions`, which is a wrapper class to handle DARB-serialization for a set of `OrgPermission`s.
    `OrgPermission`s grant granular access to org-level operations.
* `FeaturePermissions`, which is a wrapper class to handle DARB-serialization for a set of `FeaturePermission`s.
    `FeaturePermission`s grant granular access to feature-level operations.
