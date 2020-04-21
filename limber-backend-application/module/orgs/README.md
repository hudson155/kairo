# Orgs Module

The orgs module contains the basics of a client/tenant/organization.
Be careful not to overload this module with too much information about an org.
Technically, almost everything could be related back to the org
and you could make an argument to put it in this module,
but the intention is to keep this module as slim as possible.

Orgs always have a single owner.
The owner can modify all users' permissions.
