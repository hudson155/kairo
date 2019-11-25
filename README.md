# Limber

Limber is a highly dynamic application built on Ktor and React.

## Modules

* [`limber-backend-application`](/limber-backend-application):
    Limber's backend implementation.
* [`limber-web`](/limber-web):
    Limber's web frontend.
* [`piper`](/piper):
    Piper is Limber's backend framework.

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
        First, the identity get operation (usually `getById`).
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
