rootProject.name = "kairo"

include(":bom")
include(":bom-full")

include(":kairo-application")
include(":kairo-config")
include(":kairo-darb")
include(":kairo-dependency-injection")
include(":kairo-dependency-injection:feature")
include(":kairo-feature")
include(":kairo-gcp-secret-supplier")
include(":kairo-gcp-secret-supplier:testing")
include(":kairo-health-check:feature")
include(":kairo-id")
include(":kairo-id:feature")
include(":kairo-logging")
include(":kairo-protected-string")
include(":kairo-reflect")
include(":kairo-rest")
include(":kairo-rest:feature")
include(":kairo-rest-feature") // TODO: Don't commit this. This is Kairo 5.
include(":kairo-rest-feature:cors") // TODO: Don't commit this. This is Kairo 5.
include(":kairo-rest-feature:sse") // TODO: Don't commit this. This is Kairo 5.
include(":kairo-rest-feature:testing") // TODO: Don't commit this. This is Kairo 5.
include(":kairo-server")
include(":kairo-testing")
include(":kairo-util")
