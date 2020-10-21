# `limber-backend:common:rest-interface`

Endpoint specification code, shared between clients and servers.

Concrete `LimberEndpoint` classes represent requests to particular endpoints
with particular parameters.
The server uses concrete `LimberEndpoint` classes to build routing information for Ktor
(see the `common:module` module).
