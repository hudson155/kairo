rootProject.name = "limber"

include(":limber-backend:common:config")
include(":limber-backend:common:exception-mapping")
include(":limber-backend:common:exceptions")
include(":limber-backend:common:finder")
include(":limber-backend:common:ktor-auth")
include(":limber-backend:common:module")
include(":limber-backend:common:reps")
include(":limber-backend:common:rest-interface")
include(":limber-backend:common:serialization")
include(":limber-backend:common:server")
include(":limber-backend:common:sql")
include(":limber-backend:common:testing")
include(":limber-backend:common:testing-old")
include(":limber-backend:common:type-conversion")
include(":limber-backend:common:util")
include(":limber-backend:common:validation")

include(":limber-backend:deprecated:common")
include(":limber-backend:deprecated:common:module")
include(":limber-backend:deprecated:common:sql")
include(":limber-backend:deprecated:common:sql:testing")
include(":limber-backend:deprecated:common:testing")

include(":limber-backend:module:auth:module")
include(":limber-backend:module:auth:service-interface")

include(":limber-backend:module:forms:module")
include(":limber-backend:module:forms:service-interface")

include(":limber-backend:module:graphql:module")

include(":limber-backend:module:health-check")

include(":limber-backend:module:orgs:module")
include(":limber-backend:module:orgs:service-interface")

include(":limber-backend:module:users:module")
include(":limber-backend:module:users:service-interface")

include(":limber-backend:server:monolith")

include(":limber-web")
