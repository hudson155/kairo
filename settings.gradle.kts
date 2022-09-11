rootProject.name = "limber"

////////////////////////////////////////////////////////////////////////////////
// Common.
////////////////////////////////////////////////////////////////////////////////

include(":common:config")

include(":common:feature")
include(":common:feature:testing")

include(":common:logging")

include(":common:serialization")

include(":common:server")
include(":common:server:testing")

include(":common:type:protected-string")

include(":common:util")

////////////////////////////////////////////////////////////////////////////////
// Feature.
////////////////////////////////////////////////////////////////////////////////

include(":feature:health-check:client")
include(":feature:health-check:feature")
include(":feature:health-check:interface")

include(":feature:rest:client")
include(":feature:rest:feature")
include(":feature:rest:interface")
include(":feature:rest:testing")
