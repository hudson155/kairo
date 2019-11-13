# Limber Backend Framework

The Limber backend framework is a highly dynamic backend application framework built on Ktor.

When creating a new Limber project (an implementation project), your backend application should
include, the root module as a dependency. Read through the other modules below to understand what
they are and see if you need them too.

## Modules

* [`core`](/core):
    * This is the nucleus of the Limber framework.
    * Implementation project modules should always require the `core` module,
        but the implementation project application need not require the `core` module.
* [`data-conversion`](/data-conversion):
    * Responsible for converting data types.
        This is used for serialization and deserialization,
        both by registering with Jackson
        (for serializing and deserializing arbitrary objects as well as request/response bodies)
        and by registering with Ktor DataConversion
        (for serializing and deserializing request/response parameters).
* [`errors`](/errors):
    * Contains classes for JSON errors intended to be returned to the client.
        Every time something goes wrong (whether it's a 4xx error or a 5xx error),
        an error from this package should be returned.
* [`exception-mapping`](/exception-mapping):
    * Maps exceptions to JSON responses from the errors module.
* [`models`](/models):
    * Contains base classes for application models to inherit from.
        Models are used in the application/database layer.
        Models should never be used in the API layer.
* [`mongo`](/mongo):
    * Driver to interact with MongoDB, including automatic model/document mapping.
    * Include this in implementation project modules if they use MongoDB.
* [`object-mapper`](/object-mapper):
    * Custom Jackson object mapper implementation.
        It's configured with some default modules (e.g. Kotlin),
        default settings (e.g. ability to ignore unknown properties),
        and uses the `data-conversion` module for serialization/deserialization.
        It should be used across Limber.
* [`reps`](/reps):
    * Contains base classes for application reps to inherit from.
        Reps are used in the API layer.
        Models should never be used in the application/database layer.
* [`testing`](/reps):
    * Contains testing utilities.
    * Include this in implementation project modules as a test dependency
        (assuming you write tests).
* [`validation`](/validation):
    * Provides a Validator class for ad-hoc validation,
        as well as the Validation class which is used to validate reps.

## Package Structure

Because the package structure is divided between modules, it's sometimes difficult to get an idea of
the whole picture.

```
.
├── io.limberapp.framework
├── config            # Limber framework configuration
├── dataConversion    # Type conversion
│   └── conversionService # Individual type converters
├── endpoint          # Code for API endpoints
├── error             # Response body error objects
├── exceptionMapping  # Mapping exceptions to errors
├── jackson           # Code related to Jackson serialization
│   └── module            # Custom modules
│       └── conversionService # Data conversion module
│       └── mongo             # MongoDB module
│   └── objectMapper      # Custom object mapper
├── model             # Model-related boilerplate
├── module            # Code for Limber modules
├── mongo             # MongoDB ORM
├── rep               # Rep-related boilerplate
├── testing           # Testing code
├── store             # Code for Limber stores
├── util              # Util
│   └─ uuidGenerator  # Generates UUIDs
├── validation        # Used to validate models
│   └─ util               # Individual validators
└── validator         # Validator for ad-hoc validation
```
