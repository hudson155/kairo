# Piper

The Piper backend framework is a highly dynamic backend application framework built on Ktor.

When creating a new Piper project (an implementation project), your backend application should
include, the root module as a dependency. Read through the other modules below to understand what
they are and see if you need them too.

## Modules

* [`common`](/common):
    * This is the nucleus of the Piper framework.
    * Implementation project modules should always require the `common` module,
        but the implementation project application need not require the `common` module.
* [`data-conversion`](/data-conversion):
    * Responsible for converting data types.
        This is used for serialization and deserialization,
        both by registering with Jackson
        (for serializing and deserializing arbitrary objects as well as request/response bodies)
        and by registering with Ktor DataConversion
        (for serializing and deserializing request/response parameters).
* [`entities`](/entities):
    * Contains base classes for application entities to inherit from.
        Entities are used in the database layer.
        Entities should never be used in the API layer.
        Entities should only used in the application layer to interact with the database layer.
* [`errors`](/errors):
    * Contains classes for JSON errors intended to be returned to the client.
        Every time something goes wrong (whether it's a 4xx error or a 5xx error),
        an error from this package should be returned.
* [`exception-mapping`](/exception-mapping):
    * Maps exceptions to JSON responses from the errors module.
* [`exceptions`](/exceptions):
    * Exception base classes.
* [`ktor-auth`](/ktor-auth):
    * Helps configure authentication for Ktor.
* [`sql`](/sql):
    * Driver to interact with a SQL database.
    * Include this in implementation project modules if they use a SQL database.
* [`object-mapper`](/object-mapper):
    * Custom Jackson object mapper implementation.
        It's configured with some default modules (e.g. Kotlin),
        default settings (e.g. ability to ignore unknown properties),
        and uses the `data-conversion` module for serialization/deserialization.
        It should be used across Piper.
* [`reps`](/reps):
    * Contains base classes for application reps to inherit from.
        Reps are used in the API layer.
        Reps should never be used in the application layer.
        Reps should never be used in the database layer.
* [`testing`](/reps):
    * Contains testing utilities.
    * Include this in implementation project modules as a test dependency
        (assuming you write tests).
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
├── authentication    # Piper authentication configuration
├── config            # Piper framework configuration
├── dataConversion    # Type conversion
│   └── conversionService # Individual type converters
├── endpoint          # Code for API endpoints
├── entity            # Entity-related boilerplate (database layer)
├── error             # Response body error objects
├── exception         # Exceptions that might be thrown
├── exceptionMapping  # Mapping exceptions to errors
├── jackson           # Code related to Jackson serialization
│   └── module            # Custom modules
│       └── conversionService # Data conversion module
│   └── objectMapper      # Custom object mapper
├── model             # Model-related boilerplate (application layer)
├── module            # Code for Piper modules
├── rep               # Rep-related boilerplate (API layer)
├── sql               # SQL driver
├── testing           # Testing code
├── store             # Code for Piper stores
├── util              # Util
│   └─ uuidGenerator  # Generates UUIDs
├── validation        # Used to validate reps
│   └─ util               # Individual validators
└── validator         # Validator for ad-hoc validation
```
