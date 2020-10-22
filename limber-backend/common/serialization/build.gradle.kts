dependencies {
  api(Dependencies.Jackson.databind)
  implementation(Dependencies.Jackson.datatypeJsr310)
  api(Dependencies.Jackson.moduleKotlin) // Not exposed by this module - provided to consumers.
}
