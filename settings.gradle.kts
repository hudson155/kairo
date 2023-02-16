rootProject.name = "limber"

// -----------------------------------------------------------------------------
// Common.
// -----------------------------------------------------------------------------

include(":common:config")

include(":common:feature")
include(":common:feature:testing")

include(":common:logging")

include(":common:serialization")
include(":common:serialization:interface")

include(":common:server")
include(":common:server:testing")

include(":common:type:optional")
include(":common:type:protected-string")

include(":common:util")

include(":common:validation")

// -----------------------------------------------------------------------------
// DB.
// -----------------------------------------------------------------------------

include(":db:limber")

// -----------------------------------------------------------------------------
// Feature.
// -----------------------------------------------------------------------------

include(":feature:auth0:feature")
include(":feature:auth0:testing")

include(":feature:google-app-engine:client")
include(":feature:google-app-engine:feature")
include(":feature:google-app-engine:interface")

include(":feature:health-check:client")
include(":feature:health-check:feature")
include(":feature:health-check:interface")

include(":feature:organization:auth")
include(":feature:organization:client")
include(":feature:organization:feature")
include(":feature:organization:interface")

include(":feature:rest:client")
include(":feature:rest:feature")
include(":feature:rest:interface")
include(":feature:rest:testing")

include(":feature:sql:feature")
include(":feature:sql:testing")

// -----------------------------------------------------------------------------
// Server.
// -----------------------------------------------------------------------------

include(":server:monolith")
