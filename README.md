# Limber

Limber is a highly dynamic application framework built on Ktor and React.

## Modules

* [`limber-backend-application`](/limber-backend-application):
    Limber's back end implementation.
* [`limber-backend-framework`](/limber-backend-framework):
    Limber's back end framework.
* [`limber-web`](/limber-web):
    Limber's web front end.

## Conventions

1. Refer to exceptions as exceptions, not as errors.
    Do not create classes called SomethingError. Favor Something Exception instead.
1. Prefer early-return.
    It's better to do an early return than introduce additional code nesting.
    Handle your exceptional cases first, and leave the rest of the method for the happy path.
