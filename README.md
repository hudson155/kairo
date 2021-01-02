# Limber

Limber is a highly dynamic application built on Ktor and React.

![Build](https://github.com/hudson155/limber/workflows/Release/badge.svg)

## Setup

### Prerequisites

You should already have Java, Node, Postgres, and IntelliJ installed.

1. Clone the repo and `cd` into it.
1. Initialize the database using
    ```
    LIMBER_CONFIG=dev LIMBER_TASK=dbReset ./gradlew limber-backend:server:monolith:run
    ```
1. Initialize the test database using the same command, but with `test` instead of `dev`.
1. Open the project in IntelliJ and use the run configurations to run the monolith, GraphQL server, React app, or
    whatever else.

## Modules

* [`limber-backend-common`](/limber-backend/common):
    Limber's backend framework, built on Ktor.
* [`limber-monolith-server`](/limber-backend/server/monolith):
    Limber's backend implementation, written in Kotlin.
    Currently a monolith, but broken into modules to allow simplified refactoring
    when moving to microservices becomes necessary.
* [`limber-graphql-server`](/limber-backend/server/graphql):
    Limber's GraphQL backend, written in Kotlin.
* [`limber-web`](/limber-web):
    Limber's web frontend, written in TypeScript and React.

## Conventions

### Kotlin

1. Refer to exceptions as exceptions, not as errors.
    Do not create classes called SomethingError. Favor Something Exception instead.
1. Prefer early-return.
    It's better to do an early return than introduce additional code nesting.
    Handle your exceptional cases first, and leave the rest of the method for the happy path.
1. CRUD/CGUD ordering.
    Keep your methods in CRUD (Create/Read/Update/Delete) order
    (CGUD because we use the term "get" to refer to R-Read operations).
    Anywhere where operations are listed
    (e.g. service or store interfaces, module endpoint list, etc.),
    use the following guidelines for method ordering:
    1. Create operation.
        Remember that operations that create subentities are U-Update operations,
        not U-Update operations.
    1. Get operations, from most to least specific.
        First, the identity get operation (usually `getByGuid`).
        Then, any other get operations that return single instances.
        Then, any other get operations.
    1. Update operations, from widest to narrowest.
        First, the generic update operation (if present).
        Then, update operations that create/modify/delete subentities.
        Keep these in CGUD order too.
        Remember that operations that create or delete subentities are U-Update operations,
        not C-Create or D-Delete operations.
    1. Delete operations, from most to least specific.
        First, the identity delete operation (usually `delete`).
        Then, any other delete operations that delete single instances.
        Then, any other delete operations.
        Remember that operations that delete subentities are U-Update operations,
        not D-Delete operations.
1. Even though they're not real words, prefer "frontend" and "backend" to "front end" and "back end".
1. Avoid `?.let { }` where you can use early return instead.
    ```kotlin
   // No
   return makeSomething()?.let { transform(it) }

   // Yes
   val something = makeSomething() ?: return null
   return transform(something)
   ```
