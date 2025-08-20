rootProject.name = "kairo"

include(":bom")
include(":bom-full")

include(":kairo-application")
include(":kairo-config")
include(":kairo-darb")
include(":kairo-dependency-injection:feature")
include(":kairo-feature")
include(":kairo-gcp-secret-supplier")
include(":kairo-gcp-secret-supplier:testing")
include(":kairo-health-check:feature")
include(":kairo-id") // TODO: Create a Feature.
include(":kairo-logging")
include(":kairo-protected-string")
include(":kairo-rest:feature")
include(":kairo-server")
include(":kairo-testing")
include(":kairo-util")
