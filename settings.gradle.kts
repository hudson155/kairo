rootProject.name = "limber"

include(":limber-backend")

include(":limber-backend:monolith")

include(":limber-backend:monolith:common")
include(":limber-backend:monolith:common:module")
include(":limber-backend:monolith:common:sql")
include(":limber-backend:monolith:common:sql:testing")
include(":limber-backend:monolith:common:testing")

include(":limber-backend:monolith:module:auth")
include(":limber-backend:monolith:module:auth:auth-application")
include(":limber-backend:monolith:module:auth:auth-rest-interface")
include(":limber-backend:monolith:module:auth:auth-service-interface")

include(":limber-backend:monolith:module:forms")
include(":limber-backend:monolith:module:forms:forms-application")
include(":limber-backend:monolith:module:forms:forms-rest-interface")
include(":limber-backend:monolith:module:forms:forms-service-interface")

include(":limber-backend:monolith:module:orgs")
include(":limber-backend:monolith:module:orgs:orgs-application")
include(":limber-backend:monolith:module:orgs:orgs-rest-interface")
include(":limber-backend:monolith:module:orgs:orgs-service-interface")

include(":limber-backend:monolith:module:users")
include(":limber-backend:monolith:module:users:users-application")
include(":limber-backend:monolith:module:users:users-rest-interface")
include(":limber-backend:monolith:module:users:users-service-interface")

include(":limber-web")

include(":piper:application")
include(":piper:config")
include(":piper:data-conversion")
include(":piper:errors")
include(":piper:exception-mapping")
include(":piper:exceptions")
include(":piper:finder")
include(":piper:ktor-auth")
include(":piper:module")
include(":piper:reps")
include(":piper:rest-interface")
include(":piper:serialization")
include(":piper:sql")
include(":piper:testing")
include(":piper:types")
include(":piper:util")
include(":piper:validation")
