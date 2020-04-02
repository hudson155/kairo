rootProject.name = "limber"

/**
 * Backend Application
 */

include(":limber-backend-application")

include(":limber-backend-application:common")
include(":limber-backend-application:common:api")
include(":limber-backend-application:common:testing")

include(":limber-backend-application:module:auth")
include(":limber-backend-application:module:auth:api")
include(":limber-backend-application:module:auth:app")

include(":limber-backend-application:module:forms")
include(":limber-backend-application:module:forms:api")
include(":limber-backend-application:module:forms:app")

include(":limber-backend-application:module:orgs")
include(":limber-backend-application:module:orgs:api")
include(":limber-backend-application:module:orgs:app")

include(":limber-backend-application:module:users")
include(":limber-backend-application:module:users:api")
include(":limber-backend-application:module:users:app")

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
include(":piper:object-mapper")
include(":piper:reps")
include(":piper:sql")
include(":piper:sql:testing")
include(":piper:testing")
include(":piper:util")
include(":piper:validation")
