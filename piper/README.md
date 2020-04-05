# Piper

The Piper backend framework is a highly dynamic backend application framework built on Ktor.

When creating a new Piper project (an implementation project), your backend application should
include the root module as a dependency. Read through the other modules below to understand what
they are and see if you need them too.

## Modules

* [`common`](/common):
    * This is the nucleus of the Piper framework.
    * Implementation project modules should always require the `common` module,
        but the implementation project application need not require the `common` module.
* [`data-conversion`](/data-conversion):
    * Responsible for converting data types.
        Leverages the `serialization` module and Ktor's DataConversion feature
        to serialize and deserialize path parameters.
* [`errors`](/errors):
    * Contains classes for JSON errors intended to be returned to the client.
        Every time something goes wrong (whether it's a 4xx error or a 5xx error),
        an error from this package should be returned.
* [`exception-mapping`](/exception-mapping):
    * Maps exceptions to JSON responses from the `errors` module.
* [`exceptions`](/exceptions):
    * Exception base classes.
* [`ktor-auth`](/ktor-auth):
    * Helps configure authentication for Ktor.
* [`reps`](/reps):
    * Contains base classes for application reps to inherit from.
        Reps are used in the API layer.
        Reps should never be used in the application layer.
        Reps should never be used in the database layer.
* [`serialization`](/serialization):
    * Custom type serialization configs.
* [`sql`](/sql):
    * Driver to interact with a SQL database.
    * Include this in implementation project modules if they use a SQL database.
* [`testing`](/testing):
    * Contains testing utilities.
    * Include this in implementation project modules as a test dependency
        (assuming you write tests).
* [`types`](/testing):
    * Contains custom types.
* [`util`](/util):
    * Contains utilities.
* [`validation`](/validation):
    * Provides a Validator class for ad-hoc validation,
        as well as the Validation class which is used to validate reps.

## Package Structure

Because the package structure is divided between modules, it's sometimes difficult to get an idea of
the whole picture.

```
.
├── com.piperframework
├── authorization      # Piper authorization configuration
├── config             # Piper framework configuration
├── contentNegotiation # Ktor HTTP content negotiation
├── dataConversion     # Type conversion
├── endpoint           # Code for REST API endpoints
├── error              # Response body error objects
├── exception          # Exceptions that might be thrown
├── exceptionMapping   # Mapping exceptions to errors
├── ktorAuth           # Ktor/Piper authorization integration
├── module             # Code for Piper modules
├── rep                # Rep-related boilerplate (API layer)
├── serialization      # Serialization and deserialization code
├── sql                # SQL driver
├── testing            # Testing code
├── types              # Custom types
├── store              # Code for Piper stores
├── util               # Util
├── validation         # Value and rep validation
└── validator          # Validator for ad-hoc validation
```
