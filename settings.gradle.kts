rootProject.name = "limber"

/**
 * Backend Application
 */

include(":limber-backend-application")

include(":limber-backend-application:common")
include(":limber-backend-application:common:interface")
include(":limber-backend-application:common:testing")

include(":limber-backend-application:module:auth")
include(":limber-backend-application:module:auth:auth-application")
include(":limber-backend-application:module:auth:auth-rest-interface")
include(":limber-backend-application:module:auth:auth-service-interface")

include(":limber-backend-application:module:forms")
include(":limber-backend-application:module:forms:forms-application")
include(":limber-backend-application:module:forms:forms-rest-interface")
include(":limber-backend-application:module:forms:forms-service-interface")

include(":limber-backend-application:module:orgs")
include(":limber-backend-application:module:orgs:orgs-application")
include(":limber-backend-application:module:orgs:orgs-rest-interface")
include(":limber-backend-application:module:orgs:orgs-service-interface")

include(":limber-backend-application:module:users")
include(":limber-backend-application:module:users:users-application")
include(":limber-backend-application:module:users:users-rest-interface")
include(":limber-backend-application:module:users:users-service-interface")

/**
 * Piper (Backend Framework)
 */

include(":piper")
include(":piper:common")
include(":piper:data-conversion")
include(":piper:errors")
include(":piper:exception-mapping")
include(":piper:exceptions")
include(":piper:ktor-auth")
include(":piper:reps")
include(":piper:serialization")
include(":piper:sql")
include(":piper:sql:testing")
include(":piper:testing")
include(":piper:types")
include(":piper:util")
include(":piper:validation")
