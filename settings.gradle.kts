rootProject.name = "limber"

include(":limber-backend:common:application")
include(":limber-backend:common:config")
include(":limber-backend:common:data-conversion")
include(":limber-backend:common:errors")
include(":limber-backend:common:exception-mapping")
include(":limber-backend:common:exceptions")
include(":limber-backend:common:finder")
include(":limber-backend:common:health-check-module")
include(":limber-backend:common:ktor-auth")
include(":limber-backend:common:module")
include(":limber-backend:common:reps")
include(":limber-backend:common:rest-interface")
include(":limber-backend:common:serialization")
include(":limber-backend:common:sql")
include(":limber-backend:common:testing")
include(":limber-backend:common:types")
include(":limber-backend:common:util")
include(":limber-backend:common:validation")

include(":limber-backend:graphql-server")

include(":limber-backend:monolith")

include(":limber-backend:monolith:common")
include(":limber-backend:monolith:common:module")
include(":limber-backend:monolith:common:sql")
include(":limber-backend:monolith:common:sql:testing")
include(":limber-backend:monolith:common:testing")

include(":limber-backend:monolith:module:auth:auth-application")
include(":limber-backend:monolith:module:auth:auth-service-interface")

include(":limber-backend:monolith:module:forms:forms-application")
include(":limber-backend:monolith:module:forms:forms-service-interface")

include(":limber-backend:monolith:module:orgs:orgs-application")
include(":limber-backend:monolith:module:orgs:orgs-service-interface")

include(":limber-backend:monolith:module:users:users-application")
include(":limber-backend:monolith:module:users:users-service-interface")

include(":limber-web")
