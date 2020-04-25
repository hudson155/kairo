# Limber

Limber is a highly dynamic application built on Ktor and React.

## Setup

Prerequisites:
Java, Postgres, and IntelliJ
(all can be installed through Homebrew).

1. Clone the repo and `cd` into it.
1. `psql limber -f db/reset.sql`.
1. From IntelliJ, run the `[backend] Run (dev)` config.
1. `psql limber -f db/insert_fixtures.sql`.
1. `echo "REACT_APP_AUTH_MECHANISM=LOCAL_STORAGE_JWT" > limber-web/.env.local`
1. From IntelliJ, run the `[web] Run` config.
1. In the browser console, run the following
    ```javascript
   localStorage.setItem('jwt', {jwt})
    ```

## Modules

* [`limber-backend-application`](/limber-backend-application):
    Limber's backend implementation.
    Uses Piper.
    Currently a monolith, but broken into modules to allow simplified refactoring
    when moving to microservices becomes necessary.
* [`limber-web`](/limber-web):
    Limber's web frontend.
* [`piper`](/piper):
    Piper is Limber's backend framework, built on Ktor.
    It's in the same repo as Limber, but has nothing to do with Limber.
    One day, this framework will move its way out of this repo and become open source.
    Until then, make sure no Limber-specific code enters this module.

## Conventions

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
