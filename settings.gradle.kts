rootProject.name = "limber"

// Common
include(":limber-backend:common:client")
include(":limber-backend:common:config")
include(":limber-backend:common:exceptions")
include(":limber-backend:common:feature")
include(":limber-backend:common:jwt")
include(":limber-backend:common:module")
include(":limber-backend:common:reps")
include(":limber-backend:common:rest-interface")
include(":limber-backend:common:serialization")
include(
    ":limber-backend:common:type-conversion:implementation",
    ":limber-backend:common:type-conversion:interface")
include(":limber-backend:common:util")
include(":limber-backend:common:validation")

// DB
include(":limber-backend:db:limber")

// Module

// Server

// Multiplatform
include(":limber-multiplatform:darb")
include(":limber-multiplatform:logging")
include(":limber-multiplatform:permissions")
